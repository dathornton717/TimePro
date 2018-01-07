import xlrd
import time
import datetime
import os
import MySQLdb
import uuid

from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.firefox.firefox_profile import FirefoxProfile
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.chrome.options import Options

# METHODS
def enable_download_in_headless_chrome(browser, download_dir):
    #add missing support for chrome "send_command"  to selenium webdriver
    browser.command_executor._commands["send_command"] = ("POST", '/session/$sessionId/chromium/send_command')

    params = {'cmd': 'Page.setDownloadBehavior', 'params': {'behavior': 'allow', 'downloadPath': download_dir}}
    browser.execute("send_command", params)

def number_to_time(num):
    #If it is a string it is a relay split
    if (isinstance(num, basestring)):
        num = num[:-1]
    #It is a number
    else:
        #Subtract 33.5 just because it was added for some reason
        num = num - 33.5
        num = num * 24 * 60 * 60
        num = round(num, 2)
        minute = num // 60
        num = num - minute * 60
        minute = str(minute)[:-2]
        second = num // 1
        hundredth = str(num - second)[2:]
        second = str(second)[:-2]
        if len(second) == 1:
            second = "0" + second
        if len(hundredth) == 1:
            hundredth = hundredth + "0"
        if minute == "0":
            num = second + "." + hundredth
        else:
            num = minute + ":" + second + "." + hundredth
    return num

def time_to_long(time):
    if ":" in time:
        time_arr = time.split(":")
        minutes = int(time_arr[0])
        time_arr = time_arr[1].split(".")
        seconds = int(time_arr[0])
        hundredths = int(time_arr[1])
        return long(minutes * 60 * 100 + seconds * 100 + hundredths)
    else:
        time_arr = time.split(".")
        seconds = int(time_arr[0])
        hundredths = int(time_arr[1])
        return long(seconds * 100 + hundredths)

def date_to_long(date):
    dates = date.split('/')
    for i, d in enumerate(dates):
        dates[i] = int(d)
    return datetime.datetime(dates[2], dates[0], dates[1])
     
def event_to_table(event):
    if event == "50 FR":
        return "50_free"
    elif event == "100 FR":
        return "100_free"
    elif event == "200 FR":
        return "200_free"
    elif event == "500 FR":
        return "500_free"
    elif event == "1000 FR":
        return "1000_free"
    elif event == "1650 FR":
        return "1650_free"
    elif event == "50 BK":
        return "50_back"
    elif event == "100 BK":
        return "100_back"
    elif event == "200 BK":
        return "200_back"
    elif event == "50 BR":
        return "50_breast"
    elif event == "100 BR":
        return "100_breast"
    elif event == "200 BR":
        return "200_breast"
    elif event == "50 FL":
        return "50_fly"
    elif event == "100 FL":
        return "100_fly"
    elif event == "200 FL":
        return "200_fly"
    elif event == "100 IM":
        return "100_im"
    elif event == "200 IM":
        return "200_im"
    elif event == "400 IM":
        return "400_im"


# MAIN
file = open('names.txt', 'r')
names = file.read().splitlines()
team_name = ""
for name in names:
    if "TEAM:" in name:
        team_name = name[5:]
        continue
    split_names = name.split(" ")
    first_name = split_names[0]
    last_name = split_names[1]

    chrome_options = Options()
    chrome_options.add_argument("--headless")
    driver = webdriver.Chrome(chrome_options=chrome_options)
    driver.set_window_size(1024, 1024)

    enable_download_in_headless_chrome(driver, "/Users/DavidThornton/Documents/TimePro")

    driver.get("https://www.usaswimming.org/Home/times/individual-times-search")
    driver.find_element_by_id('UsasTimeSearchIndividual_Index_Div_1FirstName').send_keys(first_name)
    driver.find_element_by_id('UsasTimeSearchIndividual_Index_Div_1LastName').send_keys(last_name)
    driver.find_element_by_id('UsasTimeSearchIndividual_Index_Div_1StartDate').send_keys("01/01/2000")
    driver.find_element_by_id('UsasTimeSearchIndividual_Index_Div_1EndDate').send_keys(datetime.date.today().strftime("%m/%d/%Y"))
    e = driver.find_element_by_xpath('//span[@aria-owns="UsasTimeSearchIndividual_Index_Div_1cboCourse_listbox"]')
    e.click()
    e.send_keys(Keys.DOWN)
    e.send_keys(Keys.DOWN)
    e.send_keys(Keys.DOWN)
    e.send_keys(Keys.RETURN)

    driver.find_element_by_id('UsasTimeSearchIndividual_Index_Div_1-saveButton').click()

    try:
        myElem = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, 'UsasTimeSearchIndividual_PersonSearchResults_Grid-1')))
        stop = False
        while not stop:
            element_list = myElem.find_elements_by_tag_name('td')
            for element in element_list:
                print element.text
                if element.text == team_name:
                    parent = element.find_element_by_xpath('..')
                    parent.find_element_by_class_name("pointer").click()
                    stop = True
                    break
            if not stop:
                driver.find_element_by_id('UsasTimeSearchIndividual_PersonSearchResults_Grid-1-UsasGridPager-pgNext').click()
                myElem = WebDriverWait(driver, 5).until(EC.presence_of_element_located((By.ID, 'UsasTimeSearchIndividual_PersonSearchResults_Grid-1')))
    except TimeoutException:
        pass
    try:
        myElem = WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.ID, 'UsasTimeSearchIndividual_TimeResults_Grid-1_exportExcel')))
    except TimeoutException:
        print "Loading took too much time!"

    driver.find_element_by_id('UsasTimeSearchIndividual_TimeResults_Grid-1_exportExcel').click()

    time.sleep(5)
    driver.quit()

    time_dictionary = {
        "50 FR": [],
        "100 FR": [],
        "200 FR": [],
        "500 FR": [],
        "1000 FR": [],
        "1650 FR": [],
        "50 BK": [],
        "100 BK": [],
        "200 BK": [],
        "50 BR": [],
        "100 BR": [],
        "200 BR": [],
        "50 FL": [],
        "100 FL": [],
        "200 FL": [],
        "100 IM": [],
        "200 IM": [],
        "400 IM": []
    }
    
    file_name = "Times For " + first_name + " " + last_name + ".xlsx"
    if (first_name == "A.J." and last_name == "Hernandez"):
        file_name = "Times For A.J. Hernandez"
    workbook = xlrd.open_workbook(file_name)
    xl_sheet = workbook.sheet_by_index(0)

    epoch = datetime.datetime.utcfromtimestamp(0)

    num_cols = xl_sheet.ncols   # Number of columns
    for row_idx in range(1, xl_sheet.nrows):    # Iterate through rows
        event = str(xl_sheet.cell(row_idx, 0).value)
        if "25 " in event:
            continue
        sheet_time = xl_sheet.cell(row_idx, 1).value
        sheet_time = number_to_time(sheet_time)
        sheet_time = time_to_long(sheet_time)
        date = long((date_to_long(xl_sheet.cell(row_idx, 9).value) - epoch).total_seconds() * 1000)
        current_list = time_dictionary[event]
        tuple = (sheet_time, date)
        current_list.append(tuple)

    db = MySQLdb.connect(host="localhost", user="admin", passwd="admin", db="time_pro")
    cur = db.cursor()

    #Check if the user exists in the database
    cur.execute("select * from id_to_name where first_name = '" + first_name + "' and last_name = '" + last_name + "'")
    rows = cur.fetchall()

    #User doesn't exist in the database
    id = str(uuid.uuid4())
    if len(rows) == 0:
        try:
            cur.execute("insert into id_to_name(first_name, last_name, id) values('" + first_name + "', '" + last_name + "', '" + id + "')")
            db.commit()
        except:
            db.rollback()
    else:
        id = rows[0][2]

    for key in time_dictionary:
        table_name = event_to_table(key)
        tuples = time_dictionary[key]
        for tuple in tuples:
            time_swam = tuple[0]
            date = tuple[1]
            try:
                select_sql = "select * from " + table_name + " where id = '" + id + "' and date_swam = " + str(date) + " and time_swam = " + str(time_swam)
                cur.execute(select_sql)
                rows = cur.fetchall()
                if len(rows) == 0:
                    insert_sql = "insert into " + table_name + "(id, date_swam, time_swam) values('" + id + "', " + str(date) + ", " + str(time_swam) + ")"
                    cur.execute(insert_sql)
                    db.commit()
            except:
                db.rollback()
    db.close()
    os.remove(file_name)

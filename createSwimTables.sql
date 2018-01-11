create table id_to_name (first_name varchar(255), last_name varchar(255), id varchar(36), team_name varchar(255));
create unique index idx_id_to_name_id_index on id_to_name (id);
create index idx_id_to_name_names on id_to_name (first_name, last_name);
create index idx_id_to_name_last_name on id_to_name (last_name);
create index idx_id_to_name_team_name on id_to_name (team_name);

create table 50_free (id varchar(36), date_swam bigint, time_swam int);
create index idx_50_free_id on 50_free (id);
create index idx_50_free_date on 50_free (date_swam);

create table 100_free (id varchar(36), date_swam bigint, time_swam int);
create index idx_100_free_id on 100_free (id);
create index idx_100_free_date on 100_free (date_swam);

create table 200_free (id varchar(36), date_swam bigint, time_swam int);
create index idx_200_free_id on 200_free (id);
create index idx_200_free_date on 200_free (date_swam);

create table 500_free (id varchar(36), date_swam bigint, time_swam int);
create index idx_500_free_id on 500_free (id);
create index idx_500_free_date on 500_free (date_swam);

create table 1000_free (id varchar(36), date_swam bigint, time_swam int);
create index idx_1000_free_id on 1000_free (id);
create index idx_1000_free_date on 1000_free (date_swam);

create table 1650_free (id varchar(36), date_swam bigint, time_swam int);
create index idx_1650_free_id on 1650_free (id);
create index idx_1650_free_date on 1650_free (date_swam);

create table 50_back (id varchar(36), date_swam bigint, time_swam int);
create index idx_50_back_id on 50_back (id);
create index idx_50_back_date on 50_back (date_swam);

create table 100_back (id varchar(36), date_swam bigint, time_swam int);
create index idx_100_back_id on 100_back (id);
create index idx_100_back_date on 100_back (date_swam);

create table 200_back (id varchar(36), date_swam bigint, time_swam int);
create index idx_200_back_id on 200_back (id);
create index idx_200_back_date on 200_back (date_swam);

create table 50_breast (id varchar(36), date_swam bigint, time_swam int);
create index idx_50_breast_id on 50_breast (id);
create index idx_50_breast_date on 50_breast (date_swam);

create table 100_breast (id varchar(36), date_swam bigint, time_swam int);
create index idx_100_breast_id on 100_breast (id);
create index idx_100_breast_date on 100_breast (date_swam);

create table 200_breast (id varchar(36), date_swam bigint, time_swam int);
create index idx_200_breast_id on 200_breast (id);
create index idx_200_breast_date on 200_breast (date_swam);

create table 50_fly (id varchar(36), date_swam bigint, time_swam int);
create index idx_50_fly_id on 50_fly (id);
create index idx_50_fly_date on 50_fly (date_swam);

create table 100_fly (id varchar(36), date_swam bigint, time_swam int);
create index idx_100_fly_id on 100_fly (id);
create index idx_100_fly_date on 100_fly (date_swam);

create table 200_fly (id varchar(36), date_swam bigint, time_swam int);
create index idx_200_fly_id on 200_fly (id);
create index idx_200_fly_date on 200_fly (date_swam);

create table 100_im (id varchar(36), date_swam bigint, time_swam int);
create index idx_100_im_id on 100_im (id);
create index idx_100_im_date on 100_im (date_swam);

create table 200_im (id varchar(36), date_swam bigint, time_swam int);
create index idx_200_im_id on 200_im (id);
create index idx_200_im_date on 200_im (date_swam);

create table 400_im (id varchar(36), date_swam bigint, time_swam int);
create index idx_400_im_id on 400_im (id);
create index idx_400_im_date on 400_im (date_swam);
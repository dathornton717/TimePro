package constants;

public class DatabaseConstants {
    public static final String[] CREATE_TABLES = {
            "create table id_to_name (first_name varchar(255), last_name varchar(255), id varchar(36), team_name varchar(255))",
            "create unique index idx_id_to_name_id_index on id_to_name (id)",
            "create index idx_id_to_name_names on id_to_name (first_name, last_name)",
            "create index idx_id_to_name_last_name on id_to_name (last_name)",
            "create index idx_id_to_name_team_name on id_to_name (team_name)",

            "create table free_50 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_50_free_id on free_50 (id)",
            "create index idx_50_free_date on free_50 (date_swam)",

            "create table free_100 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_100_free_id on free_100 (id)",
            "create index idx_100_free_date on free_100 (date_swam)",

            "create table free_200 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_200_free_id on free_200 (id)",
            "create index idx_200_free_date on free_200 (date_swam)",

            "create table free_500 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_500_free_id on free_500 (id)",
            "create index idx_500_free_date on free_500 (date_swam)",

            "create table free_1000 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_1000_free_id on free_1000 (id)",
            "create index idx_1000_free_date on free_1000 (date_swam)",

            "create table free_1650 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_1650_free_id on free_1650 (id)",
            "create index idx_1650_free_date on free_1650 (date_swam)",

            "create table back_50 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_50_back_id on back_50 (id)",
            "create index idx_50_back_date on back_50 (date_swam)",

            "create table back_100 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_100_back_id on back_100 (id)",
            "create index idx_100_back_date on back_100 (date_swam)",

            "create table back_200 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_200_back_id on back_200 (id)",
            "create index idx_200_back_date on back_200 (date_swam)",

            "create table breast_50 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_50_breast_id on breast_50 (id)",
            "create index idx_50_breast_date on breast_50 (date_swam)",

            "create table breast_100 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_100_breast_id on breast_100 (id)",
            "create index idx_100_breast_date on breast_100 (date_swam)",

            "create table breast_200 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_200_breast_id on breast_200 (id)",
            "create index idx_200_breast_date on breast_200 (date_swam)",

            "create table fly_50 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_50_fly_id on fly_50 (id)",
            "create index idx_50_fly_date on fly_50 (date_swam)",

            "create table fly_100 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_100_fly_id on fly_100 (id)",
            "create index idx_100_fly_date on fly_100 (date_swam)",

            "create table fly_200 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_200_fly_id on fly_200 (id)",
            "create index idx_200_fly_date on fly_200 (date_swam)",

            "create table im_100 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_100_im_id on im_100 (id)",
            "create index idx_100_im_date on im_100 (date_swam)",

            "create table im_200 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_200_im_id on im_200 (id)",
            "create index idx_200_im_date on im_200 (date_swam)",

            "create table im_400 (id varchar(36), date_swam bigint, time_swam int)",
            "create index idx_400_im_id on im_400 (id)",
            "create index idx_400_im_date on im_400 (date_swam)"
    };
}

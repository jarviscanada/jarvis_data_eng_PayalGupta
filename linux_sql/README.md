# Linux Cluster Monitoring Agent

## Introduction
The Linux Cluster Monitoring Agent is a tool to monitor, collect and analyze data from host machines in a 10 node cluster using CentOS 7 over a network using IPv4 through switch. The data hardware specifications and CPU usage of the Linux machines is stored in a PostgreSQL database which is provisioned using docker container in the monitoring agent. All the nodes in the cluster have the bash scripts which collects the hardware specifications and usage data and sends it to the database; the CPU usage data is sent every one minute which is scheduled using crontab to get the real time data for real time analysis. The collected data is used to provide statistics for better business decisions and resource planning.

## Quick Start

- Start a psql instance using psql_docker.sh
```bash
./scripts/psql_docker.sh create [db_username] [db_password]
```
-  Create tables using ddl.sql
```bash
psql -h psql_host -U db_username -d host_agent -f sql/ddl.sql
```
- Insert hardware specs data into the db using host_info.sh
```bash
./scripts/host_info.sh [psql host] [port] host_agent [db_username] [db_password]
```
- Insert hardware usage data into the db using host_usage.sh
```bash
./scripts/host_usage.sh psql_host psql_port host_agent db_username db_password
```
- Crontab setup
```bash
# edit cronjobs
crontab -e 
# add to crontab
* * * * * bash <your path>/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```

## Implemenation
A `psql_docker.sh` script creates a psql container using docker to host the postgreSQL database in the monitoring agent node where the other nodes are connected by switch through IPv4.

The database `host_agent` was then setup using PostgreSQL command line interface using DDL SQL queries. Using `ddl.sql` the tables `host_info` and `host_usage` were created in the host_agent database for storing hardware specifications and CPU and memory usage information respectively. 

The bash scripts were created for automation purposes, `host_info` is created to insert host hardware information into the host_info table and `host_usage` script is created to insert host resource usage data into host_usage table.

The `queries.sql` using DML SQL queries was created to retrieve information from the two tables to help answer the business question for better resource planning.

## Architecture
![architecture of Linux Cluster Monitoring](https://github.com/jarviscanada/jarvis_data_eng_PayalGupta/blob/develop/linux_sql/assets/architecture_diagram_Linux_SQL.png)


## Scripts
- psql_docker.sh: Start or stop an existing container with psql image  or create a new docker container psql_docker.
```
./scripts/psql_docker.sh start|stop|create db_username db_password
```
- host_info.sh: Collects hardware specifications and insert into the database. This script is executed once to get the hardware specifications of the nodes.
```
./scripts/host_info.sh psql_host psql_port db_name psql_user
```
- host_usage.sh: Collects linux usage data and inserts it into the database. This script is scheduled for every minute using `crontab`.
```
./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```
- crontab
```
# edit cronjobs
crontab -e 
# add to crontab
* * * * * bash <your path>/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```
- ddl.sql: Creates host_info and host_usage tables to store hardware specification data and linux usage data.
```
#execute a sql file using psql command
psql -h HOST_NAME -p 5432 -U USER_NAME -d DB_NAME -f FILE_NAME.sql
```
- queries.sql: These queries are for the infrastructure team to analyze the memory usage data and plan the resources in a better way.
1. Sorts the list of cluster nodes by number of CPUs and total memory. Through this the infrastructure team can determine the nodes which have more memory.
2. Represents the average percentage of memory used in every 5 minute intervals. Through this the team can figure out that which hosts are under utilized and which ones are over utilized.
3. Reports the nodes which fail to send every minute CPU usage status to the monitoring agent 3 times in 5 minutes. Through this the health of all the servers is checked.

## Database Modeling
The host_agent database contains 2 tables:
- `host_info`contains the Hardware Specification data for each node.

| Field  | Type | Description |
| ------ | ---- |------------ |
| id | `serial`  | Primary key for the host  |
| hostname  | `varchar`  | Full name for each host system  |
| cpu_number  | `integer` | Number of cores on the host system  |
| cpu_architecture  | `varchar`  | Architecture of the CPU |
| cpu_model | `varchar`  | Name of the CPU model |
| cpu_mhz  | `float`  | Speed of the CPU  |
| L2_cache  | `integer`  | Size of the L2 cache in kB  |
| total_mem  |  `integer` | Total memory of the host system |
| timestamp  | `timestamp`  | The time when the hardware specification were recorded in UTC |

- `host_usage` contains the data usage data for the linux resources

| Field  | Type | Description |
| ------ | ---- |------------ |
| timestamp  | `timestamp`  | Time at which the CPU usage status was recorded in UTC |
| host_id | `serial`  | Foreign key for the ID corresponding to the host_info  |
| memory_free  | `integer`  |  Amount of free memory in the node in MB |
| cpu_idle  | `integer` | Percentage of time that the CPU is idle  |
| cpu_kernal  | `integer`  | Percentage of time CPU is running kernel code |
| disk_io | `integer`  | Number of disks currently undergoing I/O processes |
| disk_available  | `integer`  | Available space in the root directory in MB |

## Test
1. The bash scripts were tested manually using command line to ensure the proper functioning of the scripts by verifying the number of arguments, expected errors got displayed and the exit code was successful.
2. The SQL queries were tested using test data to ensure the expected results were displayed.

## Improvements
- More number of queries can be added for more analysis for better resource planning.
- Visualization tools can be integrated to graphically represent the average resource utilization for easier understanding.

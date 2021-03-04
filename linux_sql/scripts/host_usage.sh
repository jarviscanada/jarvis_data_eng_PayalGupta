#!/bin/bash

# assign CLI arguments to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
export PGPASSWORD=$5

#validate arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

#Save virtual machine statistics values in variable
vmstat_out_t=$(vmstat -t)
vmstat_out_m=$(vmstat -S m)
vmstat_out=$(vmstat)
vmstat_out_d=$(vmstat -d)
disk_out=$(df -BM / )

#parse host usage using bash commands
hostname=$(hostname -f)
timestamp=$(echo "$vmstat_out_t" | awk '{print  $18, $19}' | egrep "^[0-9]" )
memory_free=$(echo "$vmstat_out_m" | awk '{print $4}' | egrep "^[0-9]")
cpu_idle=$(echo "$vmstat_out" | awk '{print $15}' | egrep "^[0-9]")
cpu_kernel=$(echo "$vmstat_out" | awk '{print $14}' | egrep "^[0-9]")
disk_io=$(echo "$vmstat_out_d" | awk '{print $10}' | egrep "^[0-9]")
disk_available=$(echo "$disk_out" | awk '{print $4}' | egrep "^[0-9]" | tr -d M)

# construct the INSERT statement
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES ('$timestamp', (SELECT id FROM host_info WHERE hostname = '$hostname'), $memory_free, $cpu_idle, $cpu_kernel, $disk_io, $disk_available)"

#insert data in the host_agent database using insert statement

psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -c "$insert_stmt"

exit $?

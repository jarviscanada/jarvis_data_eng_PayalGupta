--group host by hardware info
SELECT
    cpu_number,
	id as host_id,
	total_mem
	FROM
		(
    		SELECT
        	cpu_number,
        	id,
        	total_mem,
        	rank() OVER (PARTITION BY cpu_number)
    	FROM
        	public.host_info
		)  as hardware_info
order by cpu_number, total_mem DESC;

--Function to get timestamp 5 minute interval
CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
    RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END;
$$
	LANGUAGE PLPGSQL;

-- Average memory usage query
SELECT
	   hu.host_id,
	   hi.hostname,
	   round5(hu.timestamp) as timestamp_val,
	   AVG(((hi.total_mem/1000- hu.memory_free) *100 / (hi.total_mem/1000))) as avg_used_mem_percentage
FROM
	host_usage hu
JOIN
	host_info hi
ON
	hu.host_id = hi.id
GROUP BY
	hu.host_id, timestamp_val, hi.hostname
ORDER BY
	hu.host_id, timestamp_val
	
--Detect host failure
SELECT
        host_id,
        round5(timestamp) AS ts,
        COUNT(*) AS num_data_points
FROM
    host_usage
GROUP BY
    ts,
    host_id
HAVING
    COUNT(*) < 3
ORDER BY
    host_id;



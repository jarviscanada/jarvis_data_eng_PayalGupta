-- switch to 'host_agent database'
\c host_agent

--create `host_info` table if not exist

create table if not exists PUBLIC.host_info
(

	id               	SERIAL NOT NULL,
	hostname         	VARCHAR NOT NULL,
	cpu_number			INTEGER NOT NULL,
	cpu_architecture	VARCHAR NOT NULL,
	cpu_model			VARCHAR NOT NULL,
	cpu_mhz				FLOAT NOT NULL,
	L2_cache			INTEGER NOT NULL,
	total_mem			INTEGER NOT NULL,
	"timestamp"			TIMESTAMP NOT NULL,

	PRIMARY KEY (id),
    UNIQUE (hostname)
);

--create `host_usage` table if not exist
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage
(
	"timestamp"    	TIMESTAMP NOT NULL,
	host_id        	SERIAL NOT NULL,
	memory_free		INTEGER NOT NULL,
	cpu_idle		FLOAT NOT NULL,
	cpu_kernel		FLOAT NOT NULL,
	disk_io			INTEGER NOT NULL,
	disk_available	INTEGER NOT NULL,

	FOREIGN KEY (host_id)
	REFERENCES PUBLIC.host_info(id)
);
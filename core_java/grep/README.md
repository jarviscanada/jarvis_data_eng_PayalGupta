# Introduction
The `Java Grep Application` is the simulation of the Linux grep command in 
Java. The application takes the regex and the directory to search the pattern recursively in all the files and directories and output the matching lines to another file. The application was implemented using `Java8` with two methods: Loops
and `Lambda and Stream functions`, the dependencies were managed using `Maven` and it was deployed
through `Docker`.

# Quick Start
The application can be run using the 2 methods below:
1. Maven
```
$mvn clean package
$ java -cp target/*.jar <class_file> <regex/pattern> <directory> <out_file>
```
2. Docker
```
$ docker pull payalgupta98558/grep  
$ docker run --rm -v {rootPath} -v {outFile} payalgupta98558/grep {regex} {rootPath} {outFile}
```

# Implemenation
## Pseudocode
The two implementations of the grep application are:

1.JavaGrepImp
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```
2. JavaGrepLambdaImp
```
listFiles(getRootPath()).stream()
.forEach(file-> {writeToFile(readLines(file).
stream().filter(x -> containsPattern(x))
.collect(Collectors.toList()));       
```

## Performance Issue
The Java Grep needs a lot of memory while processing through lists 
and loops while creating objects for each individual module. 
The solution to the issue was Buffered reader or Stream APIs and both
of the approaches were used in the application.

# Test
The testing was performed using a test text data file 
and the patterns were matched against this test file for 
the entered regex. 

# Deployment
The deployment was done by creating an `image` through a `Dockerfile` and then
uploading it to the `Docker Hub` for convenient distribution of the app to the
users.

# Improvement
1. The memory management can be improved to accommodate large memory requirements for the Java Grep
implementations.
2. The depth of the traversing directories can be added to the input argument.
3. The other grep functionalities can be added

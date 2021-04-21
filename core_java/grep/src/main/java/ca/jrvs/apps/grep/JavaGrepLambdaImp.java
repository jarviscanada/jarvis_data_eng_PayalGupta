package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{
    public static void main(String[] args) throws IOException {
        if (args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try{
            javaGrepImp.process();
        }catch (Exception ex) {
            javaGrepImp.logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void process() throws IOException {
        try {
          listFiles(getRootPath()).stream().forEach(file-> {
                try {
                    writeToFile(readLines(file).stream().filter(x -> containsPattern(x)).collect(Collectors.toList()));
                } catch (IOException e) {
                   logger.error("Process not completed successfully",e);
                }
            });
        }catch(Exception e){
            logger.error("Process cannot be completed", e);
        }
    }

    @Override
    public List<File> listFiles(String rootDir) throws IOException{
        Path path = Paths.get(rootDir);
        Stream<File> files = Files.walk(path).map(Path::toFile);
        return files.filter(File::isFile).collect(Collectors.toList());
    }

    @Override
    public List<String> readLines(File inputFile) throws IOException{
       Stream<String> lines = Files.lines(inputFile.toPath());
       return lines.collect(Collectors.toList());
    }
}



package ca.jrvs.apps.grep;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class JavaGrepImp implements JavaGrep{
    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

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
            List<String> matchedLines = new ArrayList<>();
            for (File file : listFiles(getRootPath())) {
                for (String line : readLines(file)) {
                    if (containsPattern(line)) {
                        matchedLines.add(line);
                    }
                }
            }
            writeToFile(matchedLines);
        }catch(Exception e){
            logger.error("Process cannot be completed", e);
        }
    }

    @Override
    public List<File> listFiles(String rootDir) throws IOException{
        List<File> fileList = new ArrayList<>();
        File[] dir = new File(rootDir).listFiles();
        for (File file : dir){
            if (file.isDirectory()){
                fileList.addAll(listFiles(file.getAbsolutePath()));
            } else if (file.isFile()){
                fileList.add(file);
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) throws IOException{
        List <String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        }catch(IOException e){
            logger.error("Error while reading file", e);
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        return (Pattern.matches(getRegex(), line));
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getOutFile()));
            for (String line : lines) {
                writer.write(line);
                //writer.close();
            }
            writer.close();
        }catch(Exception e){
            logger.error("Error while writing to file", e);
        }
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath=rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex=regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile=outFile;
    }
}
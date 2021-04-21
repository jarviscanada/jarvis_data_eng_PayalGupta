package ca.jrvs.apps.practice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc{

    @Override
    public boolean matchJpeg(String filename) {
        //regex to match file with jpg or jpeg extension
        String regex="^.*\\.jp[e]?g$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);
        return matcher.find();
    }

    @Override
    public boolean matchIp(String ip) {
        //to simplify the problem, IP address range is from 0.0.0.0 to 999.999.999.999
        String regex="\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }


    @Override
    public boolean isEmptyLine(String line) {
        //regex to check if line is empty(e.g. empty, white space, tabs, etc..)
        String regex="^\\s$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }
}

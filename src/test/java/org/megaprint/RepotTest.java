package org.megaprint;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Baurz on 4/29/2017.
 */
public class RepotTest extends CommonFunctions{

    static String session=null;
    Properties prop=new Properties();
    static String lastIssueId=null;

    @BeforeTest
    public void init() throws IOException {
        session=login();
        FileInputStream fis=new FileInputStream("data\\env.properties");
        prop.load(fis);
    }
    @DataProvider
    public Object [][] testData1(){
        return new Object[][]{  {"Issue 1","Body of Issue 1 is not empty and have some data in"},
                                {"Issue 2","Body of Issue 2 is not empty and have some data in"},
                                {"Issue 3","Body of Issue 3 is not empty and have some data in"},
                                {null,"Body of null title"},
                                {"Title of null body", null},
                                {null, null}
        };
    }
    @Test(dataProvider = "testData1",priority=1)
    public void createIssue(String issueTitle, String issueBody){
        String issueId=null;
        Assert.assertNotNull(session);
        issueId=createReport(session, issueTitle,issueBody,prop.getProperty("PROJECT_NAME"));
        Assert.assertNotNull(issueId);
        System.out.println(issueId);
        lastIssueId=issueId;

    }
    @Test(priority=2)
    public void deleteIssue(){
        int a=Integer.valueOf(lastIssueId);
        Assert.assertNotNull(session);
        for(int i=0;i<6;i++)
            Assert.assertTrue(deleteIssue(session,a--));
    }

}

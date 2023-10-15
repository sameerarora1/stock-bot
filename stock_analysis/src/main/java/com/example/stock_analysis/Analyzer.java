package com.example.stock_analysis;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
public class Analyzer {
        public static final String API_URL = "https://financialmodelingprep.com/api/v3/";
        public static final String API_KEY = "PM73xGi9w6bSHkc4CXTDkIC8uq8WnodC";
        private String output = "Please Enter Valid Stock Ticker";
        private String initialString = "";
        public static JSONArray fetchData(String endpoint, String ticker) throws Exception {
            URL url = new URL(API_URL + endpoint + ticker + "?apikey=" + API_KEY);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                Object obj = new JSONParser().parse(reader);
                return (JSONArray) obj;
            }
        }
        public Ana dataObjectCreator(String companyName) throws Exception{
            ArrayList<Long> list = new ArrayList<Long>();
            // JSONArray companyFullName = fetchData("name/", companyName);
            JSONArray incomeStatements = fetchData("income-statement/", companyName);
            // Thread.sleep(500);
            for(int i = 0; i < 5; i++){
                JSONObject setIS = (JSONObject)incomeStatements.get(i);
                long netIncome  = (long)setIS.get("netIncome");
                list.add(i, netIncome);
            }
            JSONObject setIS = (JSONObject) incomeStatements.get(0);
            // Thread.sleep(1000);
            JSONObject setP = (JSONObject)fetchData("quote-short/", companyName).get(0);
            // Thread.sleep(1000);
            JSONObject setMC = (JSONObject)fetchData("market-capitalization/", companyName).get(0);
            double pMargin = (double)setIS.get("netIncomeRatio");
            long mCap = (long)setMC.get("marketCap");
            double price = (double)setP.get("price");
            double eps = (double)setIS.get("eps");
            double peRatio = price/eps;
            initialString = "";
            initialString += String.format("<h2><br> %s BASIC FINANCIAL METRICS</h2><br>", companyName);
            NumberFormat numberFormat = NumberFormat.getInstance();
            initialString += String.format("<b>Price: </b>$%f<br>", price);
            initialString += String.format("<b>Market Cap: </b>$%s<br>", numberFormat.format(mCap));
            initialString += String.format("<b>EPS: </b>%.2f<br>", eps);
            initialString += String.format("<b>P/E Ratio: </b>%f<br>", peRatio);
            initialString += String.format("<b>Profit Margin: </b>%f<br>", pMargin);
            for(int i = 0; i<5; i++){
                if(i==0)
                    initialString += String.format("<b>Net Income Most Recent FY: </b>$%s<br>",numberFormat.format(list.get(i)));
                else
                    initialString += String.format("<b>Net Income %d FY ago: </b>$%s<br>", i, numberFormat.format(list.get(i)));
            }
            return new Ana(list, pMargin, peRatio, mCap);
        }
        public static String ratingPrinter(Ana anaObj){
            double totalScore = anaObj.totalScore();
            if(totalScore >= 0 && totalScore < 10)
                return ("Very Bad");
            else if(totalScore >= 10 && totalScore < 20)
                return ("Bad");
            else if(totalScore >=20 && totalScore < 30)
                return ("Moderately Bad");
            else if(totalScore >= 30 && totalScore < 40)
                return ("Moderately Good");
            else if(totalScore >= 40 && totalScore < 50)
                return ("Good");
            else
                return ("Very Good");
        }
        public static ArrayList<Double> scoresListCreator(Ana anaObj){
            ArrayList<Double> scoresList = new ArrayList<Double>();
            scoresList.add(0, anaObj.mCapScore());
            scoresList.add(1, anaObj.volResScore());
            scoresList.add(2, anaObj.peRatioScore());
            scoresList.add(3, anaObj.profGrowthScore());
            scoresList.add(4, anaObj.signProfScore());
            scoresList.add(5, anaObj.pMarginScore());
            return scoresList;
        }
        public String scoresOrderedListPrinter(Ana anaObj, ArrayList<Double> scoresList){
            ArrayList<Double> unsortedList = scoresListCreator(anaObj);
            Collections.sort(scoresList);
            String initialString = "";
            for(int i = 0; i<6; i++){
                for(int j = 0; j<6; j++){
                    if(unsortedList.get(j).equals(scoresList.get(i))){
                        if(j == 0){
                            initialString += String.format("<b>Market Cap Score: </b>%.2f<br>", anaObj.mCapScore());
                            unsortedList.set(j, -1.0);
                        } else if(j == 1){
                            initialString += String.format("<b>Profit Volatility Resilience Score: </b>%.2f<br>", anaObj.volResScore());
                            unsortedList.set(j, -1.0);
                        } else if (j == 2) {
                            initialString += String.format("<b>P/E Ratio Score: </b>%.2f<br>", anaObj.peRatioScore());
                            unsortedList.set(j, -1.0);
                        } else if (j == 3) {
                            initialString += String.format("<b>Profit Growth Score: </b>%.2f<br>", anaObj.profGrowthScore());
                            unsortedList.set(j, -1.0);
                        } else if (j == 4) {
                            initialString += String.format("<b>Sign of Profit Score: </b>%.2f<br>", anaObj.signProfScore());
                            unsortedList.set(j, -1.0);
                        } else {
                            initialString += String.format("<b>Profit Margin Score: </b>%.2f<br>", anaObj.pMarginScore());
                            unsortedList.set(j, -1.0);
                        }
                    }
                }
            }
            return initialString;
        }
        // public static void generateSummary(String ticker){
            // try {
                // ProcessBuilder processBuilder = new ProcessBuilder("/Users/nikobarciak/PycharmProjects/AITTester/venv/bin/python3",
                //         "/Users/nikobarciak/PycharmProjects/AITTester/main.py");

                // Process process = processBuilder.start();
                // OutputStream outputStream = process.getOutputStream();
                // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                // writer.write(ticker);
                // writer.write("\n");
                // writer.flush();

                // InputStream inputStream = process.getInputStream();
                // BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // printSummary(reader);
            // } catch (IOException e) {
                // e.printStackTrace();
            // }
        // }

        // public static void printSummary(BufferedReader in) throws IOException {
        //     String line;
        //     while ((line = in.readLine()) != null) {

        //         int currentIndex = 0;
        //         int nextIndex = 30;
        //         while (nextIndex < line.length()) {
        //             while (nextIndex > currentIndex && line.charAt(nextIndex) != ' ') {
        //                 nextIndex--;
        //             }
        //             System.out.println(line.substring(currentIndex, nextIndex));
        //             currentIndex = nextIndex + 1;
        //             nextIndex = currentIndex + 30;
        //         }
        //         System.out.println(line.substring(currentIndex));
        //     }
        // }
        public void analyze(String companyTicker) throws Exception {
            Ana anaObj = dataObjectCreator(companyTicker);
            output = "";
            output = String.format(
                "<b>Key:</b><br>" +
                "Total Score ≥ 0 and < 10: Very Bad<br>" +
                "Total Score ≥ 10 and < 20: Bad<br>" +
                "Total Score ≥ 20 and < 30: Moderately Bad<br>" +
                "Total Score ≥ 30 and < 40: Moderately Good<br>" +
                "Total Score ≥ 40 and < 50: Good<br>" +
                "Total Score ≥ 50 and ≤ 60: Very Good" +
                "<br><br><b>Overall Score: </b>%.2f<br>" +
                "<b>Overall Rating: </b>", anaObj.totalScore());
            // Add the result of ratingPrinter(anaObj) to the "output" string
            output += ratingPrinter(anaObj);
            output += String.format("<br><br><b>LIST OF INDIVIDUAL SCORES FROM LOWEST TO HIGHEST</b><br>");
            ArrayList<Double> scoresList = scoresListCreator(anaObj);
            output += scoresOrderedListPrinter(anaObj, scoresList);
        }

        public String getOutput() {
            return initialString + " <br> " + output;
        }

}


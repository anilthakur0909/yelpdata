package yelpdata;

import com.csvreader.CsvWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Yelpdata {

    public static class FetchYelpData {

        public ArrayList<String> fetchyelpdata(String url) throws IOException {
            Document document = null;
            Element element = null;
            int test = 1;
            while (element == null) {
                document = Jsoup.connect(url).timeout(0).get();
                element = document.getElementById("bizBox");
                if (test == 5) {
                    break;
                }
                test++;
            }
            ArrayList<String> data = new ArrayList();
            data.add(element.select("#bizInfoHeader>h1[itemprop=name]").text());
            data.add(element.select("#bizInfoContent>address>span[itemprop=streetAddress]").text());
            data.add(element.select("#bizInfoContent>address>span[itemprop=addressLocality]").text());
            data.add(element.select("#bizInfoContent>address>span[itemprop=addressRegion]").text());
            data.add(element.select("#bizInfoContent>address>span[itemprop=postalCode]").text());
            data.add(element.select("#bizInfoContent>span[itemprop=telephone]").text());
            data.add(element.select("#bizUrl").text());
            data.add(element.select("#bizRating .rating>meta").attr("content"));
            data.add(element.select("#bizRating .review-count>span").text());


            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-BusinessHours").text());
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-RestaurantsGoodForGroups").text());
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-BusinessAcceptsCreditCards").text());
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-BusinessParking").text());
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-RestaurantsAttire").text());

            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.aattr-RestaurantsPriceRange2").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-GoodForKids").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsReservations").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsDelivery").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsTakeOut").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsTableService").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-OutdoorSeating").text());
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-WiFi").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-GoodForMeal").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-Alcohol").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-NoiseLevel").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-Ambience").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-HasTV").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-Caters").text());
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-WheelchairAccessible").text());

           // System.out.println("data=" + data);



            Element ele = null;

            String element_text;
            ele = document.getElementById("rpp-count");

            element_text = ele.text();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(element_text);

            for (int i = 0; i < 3; i++) {
                matcher.find();
            }
            int reviews_count = Integer.parseInt(matcher.group());
            System.out.println(reviews_count);
            int page_count;
            if (reviews_count % 40 == 0) {
                page_count = reviews_count / 40;
            } else {
                page_count = reviews_count / 40 + 1;
            }
            System.out.println(page_count);

            int temp = 0;
       
            ArrayList<Double> rating = new ArrayList();
            ArrayList<String[]> reviewer_details = new ArrayList();

            for (int count = 0; count < page_count; count++) {
                String url1 = url + "?start=" + temp;
                System.out.println("new url" + url1);
                Elements reviews;
                Document doc1 = Jsoup.connect(url1).timeout(0).get();
                reviews = doc1.select("#reviews-other>ul>li");
                temp +=40;
                while (reviews == null || reviews.size() == 0) {
                    doc1 = Jsoup.connect(url1).timeout(0).get();
                    reviews = doc1.select("#reviews-other>ul>li");
                }
                //System.out.println("reviews" + reviews.size());
                for (Element review : reviews) {

                    Elements rating_element = review.select(".rating>meta");

                    //System.out.println(rating_element.get(0).attr("content"));
                    Double rating_star = Double.parseDouble(rating_element.get(0).attr("content").trim());

                    rating.add(rating_star);
                   // System.out.println("rating_star=" + rating_star);
                    //String reviewer_name = 
                    reviewer_details.add(new String[] {review.select("meta[itemprop=datePublished]").attr("content").trim(),review.select(".user-name>a").text(),review.select("p.reviewer_info").text(),review.select("p.review_comment[itemprop=description]").text(),rating_star.toString()});
                }


            }
            
            double rating_total = 0.0;
            for (int i = 0; i < rating.size(); i++) {
                rating_total += (double)rating.get(i);
            }
            double mean = (rating_total / rating.size());
  
            
            
            double deviation_total = 0.0;
            for (int i = 0; i < rating.size(); i++) { 
                    
                    double deviation=mean - rating.get(i);
                    deviation_total+=Math.pow(deviation,2);
                    
            }
            double variance = Math.sqrt(deviation_total / rating.size());
           
            Collections.sort(rating);
            double median =  0.0;
            if ((rating.size() % 2) == 0) {
                int middle = rating.size() / 2;
                median = (rating.get(middle) + rating.get(middle - 1)) / 2;
            } else {
                int middle = rating.size() / 2;
                median = rating.get(middle - 1);
            }
            System.out.println("mean=" + mean);
            System.out.println("variance=" + variance);
            System.out.println("The median is= " + median);
            data.add(Double.toString(mean));
            data.add(Double.toString(median));
            data.add(Double.toString(variance));
            Date date =null;
            try {
                date = new SimpleDateFormat("YYYY-MM-dd").parse(reviewer_details.get(0)[0]);
            } catch (ParseException ex) {
                Logger.getLogger(Yelpdata.class.getName()).log(Level.SEVERE, null, ex);
            }
            int days_between = Days.daysBetween(new DateTime(date), new DateTime(new Date())).getDays();
            data.add(Integer.toString(days_between));
            //System.out.println("review count is= "+reviews_count+" rating count is "+rating.size());
            javatocsv(data,reviewer_details);
            return data;
        }

        public void csvtojava() throws FileNotFoundException, IOException, InterruptedException {
            String filename = "/home/trantor/Documents/yelpdata/input/yelp_analysis.csv";
            String line = "";
            String url = "";
            String splitBy = ",";

            FileReader file = new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] csv_data = line.split(splitBy);
                url = csv_data[1];
                
                System.out.println("row inserted successfully.......");
            }



        }

        public void javatocsv(ArrayList<String> result,ArrayList<String[]> reviewer_details) throws IOException {
            String filename = "/home/trantor/Documents/yelpdata/output/Yelp_result.csv";
            boolean alreadyExists = new File(filename).exists();
            
            try {
                CsvWriter writer = new CsvWriter(new FileWriter(filename, true), ',');

                if (!alreadyExists) {
                    writer.write("marchant name");
                    writer.write("Address");
                    writer.write("City");
                    writer.write("state");
                    writer.write("zipcode");
                    writer.write("phone number");
                    writer.write("website address");
                    writer.write("averate rating ");
                    writer.write("reviews");
                    writer.write("Hours");
                    writer.write("Good for Group");
                    writer.write("accept credit cards");
                    writer.write("parking");
                    writer.write("attire");
                    writer.write("price range");
                    writer.write("good for kids");
                    writer.write("take reservation ");
                    writer.write("delivery");
                    writer.write("take out");
                    writer.write("vaiter services");
                    writer.write("out door seats");
                    writer.write("Wi Fi");
                    writer.write("good for");
                    writer.write("alcohol");
                    writer.write("noise level");
                    writer.write("ambience ");
                    writer.write("hasTV");
                    writer.write("caters");
                    writer.write("wheelchair accessible ");
                    
                    writer.write("mean");
                    writer.write("median");
                    writer.write("variance");
                    writer.write("days between");
                    writer.write("review date");
                    writer.write("reviewer name");
                    writer.write("reviewer city");
                    writer.write("review");
                    writer.write("rating");
                    
                }
                writer.endRecord();
                for(String[] details: reviewer_details){
                    for (String rs : result) {
                        writer.write(rs);
                    }
                    for(String detail: details){
                        writer.write(detail);
                    }
                    writer.endRecord();
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {
        FetchYelpData fetchdata = new FetchYelpData();
        //fetchdata.csvtojava();
        fetchdata.fetchyelpdata("http://www.yelp.com/biz/mikado-bistro-brentwood");

    }
}

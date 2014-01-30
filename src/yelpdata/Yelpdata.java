package yelpdata;

import com.csvreader.CsvWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class Yelpdata {

    public static class FetchYelpData {

        public ArrayList<String> fetchyelpdata(String url) throws IOException {
            Document doc;
            Element element = null;
            int test = 1;
            while (element == null) {
                doc = Jsoup.connect(url).timeout(0).get();
                element = doc.getElementById("bizBox");
                if (test == 5) {
                    break;
                }
                test++;
            }
            ArrayList<String> data=new ArrayList();        
           // String merchantName = 
            data.add(element.select("#bizInfoHeader>h1[itemprop=name]").text());
            //String address = 
            data.add(element.select("#bizInfoContent>address>span[itemprop=streetAddress]").text());
            //String city=
            data.add(element.select("#bizInfoContent>address>span[itemprop=addressLocality]").text());
            //String state=
            data.add(element.select("#bizInfoContent>address>span[itemprop=addressRegion]").text());
            //String zipcode=
            data.add(element.select("#bizInfoContent>address>span[itemprop=postalCode]").text());
            //String phone=
            data.add(element.select("#bizInfoContent>span[itemprop=telephone]").text());
            //String websiteaddress=
            data.add(element.select("#bizUrl").text());
            
            //String average_rating = 
            data.add(element.select("#bizRating .rating>meta").attr("content"));
            //String reviews = 
            data.add(element.select("#bizRating .review-count>span").text());

            //for col-0
            //String hours = 
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-BusinessHours").text());
            //String good_for_groups = 
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-RestaurantsGoodForGroups").text());
            //String accepts_credit_cards = 
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-BusinessAcceptsCreditCards").text());
            //String parking = 
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-BusinessParking").text());
            //String attire = 
            data.add(element.select("#bizAdditionalInfo>.col-0>dl>dd.attr-RestaurantsAttire").text());
            //for col-2
            //String price_range = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.aattr-RestaurantsPriceRange2").text());
            //String good_for_kids = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-GoodForKids").text());
            //String takes_reservations = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsReservations").text());
            //String delivery = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsDelivery").text());
            //String take_out = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsTakeOut").text());
            //String waiter_service = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-RestaurantsTableService").text());
            //String outdoor_seating = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-OutdoorSeating").text());
            //String wi_fi = 
            data.add(element.select("#bizAdditionalInfo>.col-1>dl>dd.attr-WiFi").text());
            //for col-3 
            //String good_for =
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-GoodForMeal").text());
            //String alcohol = 
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-Alcohol").text());
            //String noise_level = 
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-NoiseLevel").text());
            //String ambience = 
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-Ambience").text());
            //String has_tv = 
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-HasTV").text());
            //String caters = 
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-Caters").text());
            //String wheelchair_accessible = 
            data.add(element.select("#bizAdditionalInfo>.col-2>dl>dd.attr-WheelchairAccessible").text());

            System.out.println("data="+data);
            
            return data;

            
        }
        public void csvtojava() throws FileNotFoundException, IOException, InterruptedException {
            String filename = "/home/trantor/Desktop/CAN-Hadoop/yelp_analysis.csv";
            String line = "";
            String url = "";
            String splitBy = ",";
            
            FileReader file=new FileReader(filename);
            BufferedReader reader = new BufferedReader(file);
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] csv_data = line.split(splitBy);
                url = csv_data[1];
                javatocsv(fetchyelpdata(url));
                System.out.println("row inserted successfully.......");
            }
            
            
            
        }
            public void javatocsv(ArrayList<String> result) throws IOException {
            String filename = "/home/trantor/Desktop/Yelp_result.csv";
            boolean alreadyExists = new File(filename).exists();
            try{
            CsvWriter writer=new CsvWriter(new FileWriter(filename, true), ',');
    
                if (!alreadyExists){
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
                }  
                writer.endRecord();
                for(String rs :result){
                    writer.write(rs.toString());
                }
               
                writer.close();
            }
            catch (IOException e) {
                e.printStackTrace();
		}
 
        
        } 
    }

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {
        FetchYelpData fetchdata = new FetchYelpData();
        fetchdata.csvtojava();
        //fetchdata.fetchyelpdata("http://www.yelp.com/biz/mikado-bistro-brentwood");

    }
}

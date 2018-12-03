import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ConnectionConfiguration {

    public static Connection getConnection() {
        Connection connection = null;
        WifiTable wifiTable = new WifiTable("Wifi Table One");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://cohavebeta.mcomputing.eu :3306/cohave_v4_live?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "martinez", "K6M499bMzADWsMtU");

            Statement st = connection.createStatement();
            String sql = ("SELECT funfi_wifi_network.ssid,funfi_wifi_scan.bssid, funfi_wifi_scan.sensor_time, funfi_wifi_scan.device_id FROM funfi_wifi_scan " +
                    "INNER JOIN funfi_wifi_network ON funfi_wifi_scan.bssid = funfi_wifi_network.bssid " +
                    "WHERE funfi_wifi_scan.device_id = 677 AND funfi_wifi_scan.sensor_time >=1542568580935 " +
                    "AND funfi_wifi_scan.sensor_time <=1542911654781 " +
                    "ORDER BY funfi_wifi_scan.sensor_time DESC");
            ResultSet rs = st.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                String ssid = rs.getString("ssid");
                String bssid = rs.getString("bssid");
                Long time = rs.getLong("sensor_time");
                Integer device_id = rs.getInt("device_id");
                WifiEntry we = new WifiEntry(ssid, bssid, time, device_id);
                wifiTable.add(we);
                count++;
                //System.out.println(" SSID: " + ssid + " BSSID: " + bssid + " Timestamp: " + time + " ID: " + device_id);
            }
            System.out.println("Number of select data: " + count);
            Scan wifiScan_user1 = new Scan(wifiTable,677);
            System.out.println(wifiScan_user1.countScan());
            System.out.println(wifiScan_user1.getTreeMap());

            Area area_user1 = new Area(wifiScan_user1);
            area_user1.printArea();

            StayArea stayArea_user1= new StayArea(area_user1);
            stayArea_user1.printAreas();
            System.out.println("Every entry is Unique: " + stayArea_user1.isUnique());

            StayAreaTranslate stayAreaTranslate_user1= new StayAreaTranslate(stayArea_user1,wifiTable);
            stayAreaTranslate_user1.printAreas();

            Route route1 = new Route(wifiScan_user1,stayArea_user1);
            route1.printRoute2();

            JSONConstuctor jsonConstuctor = new JSONConstuctor(route1,stayArea_user1,stayAreaTranslate_user1);

            try(FileWriter file = new FileWriter("myJson.json")){
                file.write(jsonConstuctor.getJsonobj().toString());
                file.flush();
            }catch (IOException e){
                e.printStackTrace();
            }

            //List<Integer> arrayfortest = new ArrayList<>(Arrays.asList(0,1,2,5,0,1,2,6,7,0,1,2,3,1,0,1,2,3,27));
            //Longest_Subarray longest_subarray = new Longest_Subarray(route1.createArray(),arrayfortest);
            System.out.println("Real Route: " + route1.createArray());
            //System.out.println("Test array: " + arrayfortest);
            //System.out.println("Longest substring in arrays: " + longest_subarray.getLongest_substring());

            List<Route> parsedRoute = route1.routeParse2();
            for (Route route : parsedRoute){
                System.out.println(route.createArray());
            }

            Longest_substring longest_substring = new Longest_substring(parsedRoute);
            System.out.println("Longest substring " +longest_substring.getLongest_substrings());

            //FOR TEST DATA
            //ArrayList<List<Integer>> group = route1.routeParseR(arrayfortest);
            //System.out.println(group);
            //Longest_substring longest_substring = new Longest_substring(group);
            //System.out.println("Longest substring " +longest_substring.getLongest_substring());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Set<Integer> union(final Set<Integer> first, final Set<Integer> second) {
        final Set<Integer> copy = new HashSet<>(first);
        copy.addAll(second);
        return copy;
    }

}
/*
SELECT funfi_wifi_network.ssid, funfi_wifi_scan.bssid, funfi_wifi_scan.sensor_time, funfi_wifi_scan.device_id
FROM funfi_wifi_scan
INNER JOIN funfi_wifi_network ON funfi_wifi_scan.bssid = funfi_wifi_network.bssid
WHERE funfi_wifi_scan.device_id =677
ORDER BY funfi_wifi_scan.sensor_time DESC
LIMIT 456
 */
/*
SELECT funfi_wifi_network.ssid, funfi_wifi_scan.bssid, funfi_wifi_scan.sensor_time, funfi_wifi_scan.device_id
FROM funfi_wifi_scan
INNER JOIN funfi_wifi_network ON funfi_wifi_scan.bssid = funfi_wifi_network.bssid
WHERE funfi_wifi_scan.device_id =677
AND funfi_wifi_scan.sensor_time >=1542535802041
AND funfi_wifi_scan.sensor_time <=1542977687468
ORDER BY funfi_wifi_scan.sensor_time DESC
 */
/*
SELECT funfi_wifi_network.ssid, funfi_wifi_scan.bssid, funfi_wifi_scan.sensor_time, funfi_wifi_scan.device_id
FROM funfi_wifi_scan
INNER JOIN funfi_wifi_network ON funfi_wifi_scan.bssid = funfi_wifi_network.bssid
WHERE funfi_wifi_scan.device_id =677
AND funfi_wifi_scan.sensor_time >=1542568580935
AND funfi_wifi_scan.sensor_time <=1542911654781
ORDER BY funfi_wifi_scan.sensor_time DESC
 */
/*
DATA FOR TIME DIFF 80 minutes
SELECT funfi_wifi_network.ssid, funfi_wifi_scan.bssid, funfi_wifi_scan.sensor_time, funfi_wifi_scan.device_id
FROM funfi_wifi_scan
INNER JOIN funfi_wifi_network ON funfi_wifi_scan.bssid = funfi_wifi_network.bssid
WHERE funfi_wifi_scan.device_id =677
AND funfi_wifi_scan.sensor_time >=1542799714954
AND funfi_wifi_scan.sensor_time <=1542911654781
ORDER BY funfi_wifi_scan.sensor_time DESC
 */
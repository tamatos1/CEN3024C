import java.sql.*;

public class test2 {
    public static void main(String args[]){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sakila","root","Xojte3-dizsyb-guwsun");
//here sonoo is database name, root is username and password
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from actor");
            //while(rs.next())
                //System.out.println(rs.getInt(1)+"  "+rs.getString("first_name")+"  "+rs.getString(3));

            if (rs.next() == false) {
                System.out.println("ResultSet in empty in Java");
            } else {
                do {
                    String data = rs.getString("first_name");
                    System.out.println(data); } while (rs.next());
            }


            //System.out.println(rs.getString("first_name"));
            con.close();
        }catch(Exception e){ System.out.println(e);}
    }
}

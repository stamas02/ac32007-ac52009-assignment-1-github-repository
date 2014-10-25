package uk.ac.dundee.computing.aec.instagrim.lib;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace IF NOT EXISTS instagrim  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";
            String CreateFriendTable = "CREATE TABLE IF NOT EXISTS instagrim.friends ("
            		+"user1 text,"
            		+"user2 text,"
            		+"PRIMARY KEY (user1, user2)"
            		+")";
                       
            String CreateMessagesTable = "CREATE TABLE IF NOT EXISTS instagrim.messages ("
            		+"messagetype int,"
            		+"touser text,"
            		+"fromuser text,"
            		+"messageid timeuuid,"
            		+"message text,"
            		+"PRIMARY KEY (messagetype, touser, fromuser, messageid)"
            		+")";
            		
            String CreatePicsTable = "CREATE TABLE if not exists instagrim.pics ("
            		+"picid timeuuid PRIMARY KEY,"
            		+"folder ascii,"
            		+"image blob,"
            		+"imagelength int,"
            		+"interaction_time timestamp,"
            		+"name ascii,"
            		+"processed blob,"
            		+"processedlength int,"
            		+"thumb blob,"
            		+"thumblength int,"
            		+"type ascii,"
            		+"user ascii"
            		+")";
            
            String CreatePicsCommentTable = "CREATE TABLE if not exists instagrim.piccomments ("
            		+"picid timeuuid,"
            		+"commentid timeuuid,"
            		+"comment text,"
            		+"user ascii,"
            		+"PRIMARY KEY (picid, commentid)"
            		+")";
            
            String CreateUserPiclistTable = "CREATE TABLE if not exists instagrim.userpiclist ("
            		+"user text,"
            		+"folder text,"
            		+"picid timeuuid,"
            		+"accessability int,"
            		+"pic_added timestamp,"
            		+"picidindex timeuuid,"
            		+"PRIMARY KEY (user, folder, picid)"
            		+")";
            String CreateUserPiclistTableIndex = "CREATE INDEX if not exists userpiclist_picid ON instagrim.userpiclist (picidindex)";
            
            String CreateUserProfiles = "CREATE TABLE instagrim.userprofiles ("
            		+"login ascii PRIMARY KEY,"
            		+"dob timestamp,"
            		+"firstname ascii,"
            		+"lastname ascii,"
            		+"password ascii,"
            		+"piclength int,"
            		+"profilepic blob,"
            		+"type ascii"
            		+")";
            
            
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("created instagrim ");
            } catch (Exception et) {
                System.out.println("Can't create instagrim " + et);
            }

            String[] tables = new String[7];
            tables[0] = CreateFriendTable;
            tables[1] = CreateMessagesTable;
            tables[2] = CreatePicsTable;
            tables[3] = CreatePicsCommentTable;
            tables[4] = CreateUserPiclistTable;
            tables[5] = CreateUserPiclistTableIndex;
            tables[6] = CreateUserProfiles;
            	

            
            for (int i = 0; i < tables.length; i++)
            {
                try {
                    SimpleStatement cqlQuery = new SimpleStatement(tables[i]);
                    session.execute(cqlQuery);
                } catch (Exception et) {
                    System.out.println("Can't create tweet table " + tables[i]);
                    System.out.println(et.getMessage());
                }
            }
            
            session.close();

        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}

/**
 * Created by Aniket Kumar on 25-Sep-17.
 */

public class ClientMain {
    public static void main(String args[]){
        try{
            Socket socket = new Socket("localhost", 5000);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter client ID:");
            String clientID = sc.nextLine();

            // repeatedly taking input and sending to server
            while(true){
                String msg = sc.nextLine();
                dos.writeUTF("I am client with ID " + clientID + " " + msg);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import frc.robot.gamepad.OI;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.Timer;


public class UDP extends SubsystemBase{
    public static float varX;
    public static float varY;
    public static float proverka = (float) 0.5;

    private static final AutoTrain auto = RobotContainer.AutoTrain;
    private static final OMS oms = RobotContainer.oms;
    private static final OI oi = RobotContainer.oi;
    DatagramSocket socket = null;
    boolean sk;
    private static final int BUFFER_SIZE = 1024;
    boolean nullPose = false;



    public UDP(){
        
    }



    public void udpserver() {
        new Thread(() -> {
            final int PORT = 8080;
    
            try {
                socket = new DatagramSocket(PORT);
                System.out.println("Server ready on port " + PORT);
    
                byte[] receiveData = new byte[BUFFER_SIZE];
    
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
    
                    byte[] data = reverseBytes(receivePacket.getData(), receivePacket.getLength());
                    float[] numbers = bytesToFloats(data);
                    boolean isDrone = bytesToBoolean(data);

                    auto.coordx = numbers[0];
                    auto.coordy = numbers[1];
                    auto.isDrone = isDrone;
    
                    if (numbers.length == 2) {
                        System.out.println("Received coordinates: x = " + auto.coordx + ", y = " + auto.coordy);
                        System.out.println("isDrone: " + auto.isDrone);
                    } else {
                        System.out.println("Received incorrect number of values. Expected 2, received: " + numbers.length);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }).start();
    }

    private static byte[] reverseBytes(byte[] data, int length) {
        byte[] reversed = new byte[length];
        for (int i = 0; i < length; i++) {
            reversed[i] = data[length - 1 - i];
        }
        return reversed;
    }

    private static float[] bytesToFloats(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        float[] floats = new float[(bytes.length - 1) / 4];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = buffer.getFloat();
        }
        return floats;
    }

    private static boolean bytesToBoolean(byte[] bytes) {
        return bytes[bytes.length - 1] != 0;
    }
    
    public float getVarX() {
        return varX;
    }
    
    public float getVarY() {
        return varY;
    }
    
    public void closeServ(){
        socket.close();
    }

    public void poisk(){
        int itsn = 0;

        sk = oi.getDriveLeftTrigger();
        if (auto.coordy <= (float) 0.4 && auto.koncniz() == false){//0.4//0.46
            oms.setVverhMotorSpeed(0.2);
            auto.setKlapan(true);
            itsn = 0;
        }
        else if(auto.coordy >= (float) 0.6 && auto.koncverh() == false){///0.6//0.54
            oms.setVverhMotorSpeed(-0.4);
            auto.setKlapan(true);
            itsn = 0;
        }
        else if(auto.coordy > (float) 0.4 && auto.coordy < (float) 0.6){
            oms.setVverhMotorSpeed(0);
            auto.setKlapan(true);
            itsn = 0;
        }
        if(auto.coordx <= (float) 0.4){//0.4//0.46
            auto.setPovorotMotorSpeed(-0.3);
            auto.setKlapan(true);
            itsn = 0;
        }
        else if(auto.coordx >= (float) 0.6){//0.6//0.54
            auto.setPovorotMotorSpeed(0.3);
            auto.setKlapan(true);
            itsn = 0;
        }
        else if(auto.coordx > (float) 0.4 && auto.coordx < (float) 0.6){
            auto.setPovorotMotorSpeed(0);
            auto.setKlapan(true);
            itsn = 0;
        }


        if(itsn == 0 ){
            if (auto.coordx > (float) 0.4 && auto.coordx < (float) 0.6 && auto.coordy > (float) 0.4 && auto.coordy < (float) 0.6){
                if (auto.isDrone){
                    if(auto.cobra() > 1700){
                        clopen(true);
                    }
                    else if (auto.cobra() < 1500){
                    clopen(false);
                        itsn++;
                    }
                }
            }
        }
    }

    public void clopen(boolean cl){
        if (cl)
        {            
            auto.setKlapan(false);
        }
        else{
            auto.setKlapan(true);
        }
    }

    public void nullposition(){
        while(auto.koncniz() == false){
            oms.setVverhMotorSpeed(-0.4);
        }
        oms.setVverhMotorSpeed(-0.4);
        Timer.delay(2);
        oms.setVverhMotorSpeed(0);
        nullPose = true;
    }

    @Override
    public void periodic()
    {
        
    }
}
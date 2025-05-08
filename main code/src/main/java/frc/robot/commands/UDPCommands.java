package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.UDP;
import frc.robot.subsystems.AutoTrain;
import frc.robot.subsystems.OMS;


public class UDPCommands extends CommandBase
{
    private static final UDP udp = RobotContainer.udp;
    private static final AutoTrain auto = RobotContainer.AutoTrain;
    private static final OMS oms = RobotContainer.oms;

    boolean getNewBarcode;
    int inz = 0;

    public UDPCommands ()
    {
        addRequirements(udp);
    }

    public void nullVerf(){
        
        if (auto.koncverh() == false && inz == 0){
            oms.setVverhMotorSpeed(0.4);}
        else if(auto.koncniz() == false && inz == 1){
            oms.setVverhMotorSpeed(-0.4);
        }
        if (auto.koncverh() == true){
            inz = 1;
        }
    }

    @Override
    public void initialize(){
        udp.clopen(false);
    }

    @Override
    public void execute()
    {
        udp.udpserver();
        udp.poisk();
    }
    

    @Override
    public void end(boolean interrupted){
        udp.clopen(false);
        inz = 0;
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
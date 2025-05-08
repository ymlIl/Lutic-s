package frc.robot.commands.autoCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AutoTrain;
import frc.robot.subsystems.OMS;
import edu.wpi.first.wpilibj.Timer;

public class SimpleAuto extends CommandBase {
    private static final AutoTrain auto = RobotContainer.AutoTrain;
    private static final OMS oms = RobotContainer.oms;

    int inz =0;

    public SimpleAuto(double x, double y, double z) {
        addRequirements(auto);
    }

    public void nullposition(){
        while(auto.koncniz() == false){
            oms.setVverhMotorSpeed(0.4);
        }
        oms.setVverhMotorSpeed(-0.6);
        Timer.delay(1);
        oms.setVverhMotorSpeed(0);
    }
        
    @Override
    public void initialize() {
        //nullposition();
    }
    

    @Override
    public void execute(){

    }

    @Override
    public void end (boolean interrupted)
    {
        auto.setPovorotMotorSpeed(0.0);
        inz = 0;
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }
}
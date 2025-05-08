package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AutoTrain;
import frc.robot.subsystems.OMS;
import frc.robot.gamepad.OI;

public class TeleopOMS extends CommandBase 
{
    private static final OMS oms = RobotContainer.oms;
    private static final OI oi = RobotContainer.oi;
    private static final AutoTrain auto = RobotContainer.AutoTrain;

    double inputLeftY = 0;
    double inputLeftX = 0;
    double inputRightX = 0;

    double deltaLeftY = 0;
    double deltaLeftX = 0;
    double deltaRightX = 0;
    double prevLeftY = 0;
    double prevLeftX = 0;
    double prevRightX = 0;

    private static final double RAMP_UP     = 0.05;

    /**
     * Ramp down Constant
     */
    private static final double RAMP_DOWN   = 0.05;

    /**
     * Delta Limit
     */
    private static final double DELTA_LIMIT = 0.075;

    /**
     * Joystick inputs
     */
    boolean setServo;
    boolean sk;

    /**
     * Constructor
     */
    public TeleopOMS ()
    {
        addRequirements(oms);
    }

    public void toms(){
        inputLeftX = oi.getLeftDriveX();
        inputLeftY = - oi.getRightDriveY();
        inputRightX = oi.getRightDriveX();

        deltaLeftX = inputLeftX - prevLeftX;
        deltaLeftY = inputLeftY - prevLeftY;
        deltaRightX = inputRightX - prevRightX;
        if(deltaLeftX >= DELTA_LIMIT)
            inputLeftX += RAMP_UP;
        else if (deltaLeftX <= -DELTA_LIMIT)
            inputLeftX -= RAMP_DOWN;
        if(deltaLeftY >= DELTA_LIMIT)
            inputLeftY += RAMP_UP;
        else if (deltaLeftY <= -DELTA_LIMIT)
            inputLeftY -= RAMP_DOWN;
        if(deltaRightX >= DELTA_LIMIT)
            inputRightX += RAMP_UP;
        else if (deltaRightX <= -DELTA_LIMIT)
            inputRightX -= RAMP_DOWN;
        prevLeftY = inputLeftY;
        prevLeftX = inputLeftX;
        prevRightX = inputRightX;

        if (inputLeftY > 0) {
            if (auto.koncniz() == false){
                oms.setVverhMotorSpeed(inputLeftY);
            }
            else{
                oms.setVverhMotorSpeed(0);
            };
        }
        if (inputLeftY < 0) {
            if (auto.koncverh() == false){
                oms.setVverhMotorSpeed(inputLeftY);
            }
            else{
                oms.setVverhMotorSpeed(0);
            };
        }


        sk = oi.getDriveLeftTrigger();;
        setServo = oi.getDriveRightTrigger();
        if (sk)
        {            
            auto.setKlapan(false);
        }
        else{
            auto.setKlapan(true);
        }

        
        if (setServo)
        {
            oms.setCamPov(-1);
        }
        if(!setServo){
            oms.setCamPov(0);
        }
    }

    @Override
    public void initialize()
    {
        oms.resetEncoders();
        auto.setKlapan(true);
    }

    @Override
    public void execute()
    {
        // toms();
    }

    @Override
    public void end(boolean interrupted)
    {
        oms.setVverhMotorSpeed(0.0);
        auto.setKlapan(true);
    }

    @Override
    public boolean isFinished()
    {
        return false;
    } 
}
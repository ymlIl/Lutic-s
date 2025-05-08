package frc.robot.subsystems;

import com.studica.frc.ServoContinuous;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class OMS extends SubsystemBase
{
    private TitanQuad verhL;
    private ServoContinuous cam;
    public String line;

    private TitanQuadEncoder VerhLEncoder;

    private ShuffleboardTab tab = Shuffleboard.getTab("Turel"); 
    private NetworkTableEntry VerhLEncoderValue = tab.add("Encoder", 0)
                                                    .getEntry();

    public OMS ()
    {
        verhL = new TitanQuad(Constants.TITAN_ID, Constants.M2);
        cam = new ServoContinuous(0);

        VerhLEncoder = new TitanQuadEncoder(verhL, Constants.M2, Constants.ELEVATOR_DIST_TICK);
    }

    /**
     * @param speed
     */
    public void setVverhMotorSpeed(double speed)
    {
        verhL.set(speed);
    }


    /**
     * @return distance traveled in mm
     */
    public double getVerhLEncoderDistance()
    {
        return VerhLEncoder.getEncoderDistance() * -1;
    }


    public void setCamPov(double speed)
    {
        cam.set(speed);
    }

    public void resetEncoders()
    {
        VerhLEncoder.reset();
    }


    @Override
    public void periodic()
    {
        VerhLEncoderValue.setDouble(getVerhLEncoderDistance());
    }

}
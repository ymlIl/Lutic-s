package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.studica.frc.TitanQuad;
import com.studica.frc.TitanQuadEncoder;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import com.studica.frc.Cobra;

public class AutoTrain extends SubsystemBase
{   
    private TitanQuad povorot;
    private TitanQuadEncoder povorotEncoder;

    private AHRS navx;
    private DigitalOutput klapan;
    private DigitalInput koncverh;
    private DigitalInput koncniz;
    private DigitalInput koncserv;
    private Cobra cobra;
    private AnalogInput pressure;
    private static final int FILTER_SIZE = 10;
    private double[] filterBuffer = new double[FILTER_SIZE];
    private int filterIndex = 0;
    private boolean bufferFilled = false;


    public float coordx = 0.0f;
    public float coordy = 0.0f;
    public boolean isDrone = false;


    private ShuffleboardTab tab = Shuffleboard.getTab("Turel"); 
    private NetworkTableEntry povorotEncoderValue = tab.add("Povorot Encoder", 0)
                                                    .getEntry();
    private NetworkTableEntry gyroValue = tab.add("NavX Yaw", 0)
                                                    .getEntry();
    private NetworkTableEntry cobraValue = tab.add("Cobra Voltage", 0)
                                                    .getEntry();
    private NetworkTableEntry pressureValue = tab.add("pressure", 0)
                                                    .getEntry();
    private NetworkTableEntry barValue = tab.add("Bar pressure", 0)
                                                    .getEntry();
    private NetworkTableEntry barFilterValue = tab.add("filt pressure", 0)
                                                    .getEntry();


    public AutoTrain ()
    {
        povorot = new TitanQuad(Constants.TITAN_ID, Constants.M3);
        povorotEncoder = new TitanQuadEncoder(povorot, Constants.M3, Constants.WHEEL_DIST_PER_TICK);

        navx = new AHRS(SPI.Port.kMXP);
        klapan = new DigitalOutput(17);
        koncniz = new DigitalInput(0);
        koncverh = new DigitalInput(1);
        koncserv = new DigitalInput(2);
        cobra = new Cobra();
        pressure = new AnalogInput(0);
    }

    /**
     * @param speed range -1 to 1 (0 stop)
     */
    public void setPovorotMotorSpeed(double speed)
    {
        povorot.set(-speed);
    }

    /**
     * @param x axis value (-1 to 1)
     * @param y axis value (-1 to 1)
     * @param z axis value (-1 to 1)
     */
    public void holonomic(double x, double y, double z)
    {
        double rSpeed = ((x / 3) - (y / Math.sqrt(3)) + z) * Math.sqrt(3);
        double Speed = ((x / 3) + (y / Math.sqrt(3)) + z) * Math.sqrt(3);

        double max = Math.abs(rSpeed);
        if (Math.abs(Speed) > max) max = Math.abs(Speed);

        if (max > 1)
        {
            Speed /= max;
        }

        povorot.set(Speed*-1);
    }

    /**
     * @return distance traveled in mm
     */
    public double getPovorotEncoderDistance()
    {
        return povorotEncoder.getEncoderDistance();
    }

    /**
     * @return yaw angle in degrees range -180° to 180°
     */
    public double getYaw()
    {
        return navx.getYaw();
    }

    public void resetEncoders()
    {
        povorotEncoder.reset();
    }


    public void resetYaw()
    {
        navx.zeroYaw();
    }

    public void setKlapan(boolean set){
        klapan.set(set);
    }

    public boolean koncniz() 
    {
        return koncniz.get();
    }

    public boolean koncverh() 
    {
        return koncverh.get();
    }

    public boolean koncserv() 
    {
        return koncserv.get();
    }

    public double cobra(){
        return cobra.getRawValue(0);
    }
    public double value() {
        return 1;
    }

    public double getPressure()
    {
        return (pressure.getVoltage());
    }

    public double getBarValue(){
        int Phigh = 40;
        int Plow = 0;
        int Vhigh = 5;
        double Vlow = 0.484;
        double V = pressure.getVoltage();
        return ((Phigh-Plow)/(Vhigh-Vlow))*(V-Vlow)+Plow;
    }

    public double getFilteredBarValue() {
        double rawValue = getBarValue();
        filterBuffer[filterIndex] = rawValue;
        filterIndex = (filterIndex + 1) % FILTER_SIZE;

        if (filterIndex == 0) {
            bufferFilled = true;
        }

        if (!bufferFilled) {
            return rawValue;
        }

        double sum = 0;
        for (double value : filterBuffer) {
            sum += value;
        }
        return sum / FILTER_SIZE;
    }

    @Override
    public void periodic()
    {
        povorotEncoderValue.setDouble(getPovorotEncoderDistance());
        gyroValue.setDouble(getYaw());
        cobraValue.setDouble(cobra());
        pressureValue.setDouble(getPressure());
        barValue.setDouble(getBarValue());
        barFilterValue.setDouble(getFilteredBarValue());
    }
}
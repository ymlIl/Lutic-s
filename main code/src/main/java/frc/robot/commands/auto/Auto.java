package frc.robot.commands.auto;

import frc.robot.commands.autoCommands.SimpleAuto;

public class Auto extends AutoCommand
{
    public Auto ()
    {
        super(new SimpleAuto(0.0, 0.5, 0.0));
            
    }
}
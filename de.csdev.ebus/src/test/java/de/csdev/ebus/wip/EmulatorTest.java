package de.csdev.ebus.wip;

import org.junit.Test;

import de.csdev.ebus.utils.Emulator2;

public class EmulatorTest {

    @Test
    public void xxx() {

        Emulator2 emu = new Emulator2();

        try {
            Thread.sleep(3000);

            emu.write(new byte[] { 0x00, 0x01, 0x02, 0x00, 0x01, 0x02, 0x00, 0x01, 0x02, 0x00, 0x01, 0x02, 0x00, 0x01,
                    0x02, 0x00, 0x01, 0x02, 0x00, 0x01, 0x02, 0x00, 0x01, 0x02, 0x00, 0x01, 0x02 });

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}

package com.systemj.ipc;

public class SignalGPIO_1Out extends com.systemj.ipc.GenericSignalSender {
    int INDEX;
    
    public SignalGPIO_1Out()
    {
        super();
        this.INDEX = 0;
    }
    
    @Override
    public void configure(java.util.Hashtable a)
    {
        if (!a.containsKey((Object)"Index"))
        {
            throw new RuntimeException("Index not specified in XML");
        }
        this.INDEX = Integer.valueOf((String)a.get((Object)"Index")).intValue();
    }
    
    @Override
    public void run()
    {
//        try
//        {
//            if ((String)this.buffer[1] != "high")
//            {
//                if ((String)this.buffer[1] == "low")
//                {
//                    this.setGPIO("5");
//                }
//            }
//            else
//            {
//                this.setGPIO("4");
//            }
//        }
//        catch(Exception a)
//        {
//            System.out.println((Object)a);
//        }
        this.setGPIO("4");
    }
    
    @Override
    public void arun(){
    	 this.setGPIO("5");
    }
    
    private void setGPIO(String s)
    {
        try
        {
            String[] a = new String[3];
            a[0] = "/opt/hps/gpio_out_1/gpio_out_1";
            a[1] = s;
            a[2] = Integer.toString(this.INDEX);
            int i = new ProcessBuilder(a).start().waitFor();
            if (i != 0)
            {
                System.out.println(new StringBuilder().append("[Error]: gpio_out_1 ").append(i).toString());
            }
        }
        catch(Exception a0)
        {
            System.out.println((Object)a0);
        }
    }
}

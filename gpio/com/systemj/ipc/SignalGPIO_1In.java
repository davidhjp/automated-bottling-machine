package com.systemj.ipc;

public class SignalGPIO_1In extends com.systemj.ipc.GenericSignalReceiver {
	int PINNUMBER;
	int pinValue;

	public SignalGPIO_1In()
	{
		super();
		this.PINNUMBER = 0;
		this.pinValue = 0;
	}

	public void configure(java.util.Hashtable a)
	{
		if (!a.containsKey((Object)"PinNumber"))
		{
			throw new RuntimeException("PinNumber not specified in XML");
		}
		this.PINNUMBER = Integer.valueOf((String)a.get((Object)"PinNumber")).intValue();
	}

	@Override
	public synchronized void getBuffer(Object[] a)
	{
		try
		{
			String[] a0 = new String[3];
			a0[0] = "/opt/hps/gpio_in_1/gpio_in_1";
			a0[1] = "0";
			a0[2] = Integer.toString(this.PINNUMBER);
			Process a1 = new ProcessBuilder(a0).start();
			java.io.BufferedReader a2 = new java.io.BufferedReader((java.io.Reader)new java.io.InputStreamReader(a1.getInputStream()));
			String s = a2.readLine();
			int i = (s == null) ? 0 : Integer.valueOf(s).intValue();
			a2.close();
			int i0 = a1.waitFor();
			if (i0 != 0)
			{
				java.io.PrintStream a3 = System.out;
				Object[] a4 = new Object[1];
				a4[0] = Integer.valueOf(i0);
				a3.printf("[Error]: gpio_out_1 %d.%n", a4);
			}
			if ((i & 1 << this.PINNUMBER) != 0)
			{
				a[0] = Boolean.TRUE;
				a[1] = "high";
			}
			else
			{
				a[0] = Boolean.FALSE;
				a[1] = "low";
			}
		}
		catch(Exception a5)
		{
			System.out.println((Object)a5);
		}
	}

	@Override
	public void run()
	{
	}
}

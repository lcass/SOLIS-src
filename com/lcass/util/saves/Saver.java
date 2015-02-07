package com.lcass.util.saves;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Saver {
	private FileOutputStream fos;
	private FileInputStream fis;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private boolean open = false;

	public Saver(String write, String read) {
		try {
			fos = new FileOutputStream(write);
			fis = new FileInputStream(read);
			oos = new ObjectOutputStream(fos);
			ois = new ObjectInputStream(fis);
			open = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.append("Invalid file");
			e.printStackTrace();
		}

	}

	public void close() {
		open = false;
		try {
			fos.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void open(String write, String read) {
		try {
			fos = new FileOutputStream(write);
			fis = new FileInputStream(read);
			oos = new ObjectOutputStream(fos);
			ois = new ObjectInputStream(fis);
			open = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.append("Invalid file");
			e.printStackTrace();
		}

	}

	public void save(Object a) {
		if (a == null) {
			return;
		}
		if (!open) {
			return;
		}
		try {
			oos.writeObject(a);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void save(Object[] a) {
		if (a == null) {
			return;
		}
		if (!open) {
			return;
		}
		try {
			for (int i = 0; i < a.length; i++) {
				if (a[i] != null) {
					oos.writeObject(a);
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object[] read() {
		ArrayList<Object> temp = new ArrayList<Object>();
		if (!open) {
			return null;
		}
		try {
			while (ois.available() > 0) {
				temp.add(ois.readObject());
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp.toArray();
	}
}

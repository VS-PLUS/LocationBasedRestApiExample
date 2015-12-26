package thread;

public class Mythread extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// System.out.println("Hi I am:"+Thread.currentThread().getName());
		for (int i = 0; i < 20; i++) {
			System.out.println(Thread.currentThread().getName() + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		try {
			Mythread t1 = new Mythread();
			t1.setName("ram");
			Mythread t2 = new Mythread();
			t2.setName("shyaam");

			Mythread t3 = new Mythread();
			t3.setName("sita");
			t1.start();
			t1.join();
			t2.start();
			t2.join();
			t3.start();
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

package darkforces;
import javax.media.opengl.*;

import com.jogamp.newt.Window;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

public class GameComponent implements Runnable {

	private static final int FPS = 60;
	private FPSAnimator animator;
	
	static {
		GLProfile.initSingleton();
	}
	
	private GLCapabilities getCaps(GLProfile glp) {
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setPBuffer(false);
		caps.setAccumAlphaBits(0);
		caps.setAccumRedBits(0);
		caps.setAccumGreenBits(0);
		caps.setAccumBlueBits(0);
		caps.setSampleBuffers(false);
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		caps.setDepthBits(24);
		caps.setStencilBits(8);
		caps.setAlphaBits(8);
		caps.setRedBits(8);
		caps.setGreenBits(8);
		caps.setBlueBits(8);
		caps.setBackgroundOpaque(true);
		return caps;
	}

	@Override
	public synchronized void run() {
		
		if (animator != null) {
			return;
		}
		
		GLProfile glp = GLProfile.getGL2GL3();
		GLCapabilities caps = getCaps(glp);
		
		GLWindow window = GLWindow.create(caps);
		window.setUpdateFPSFrames(FPSCounter.DEFAULT_FRAMES_PER_INTERVAL, System.out);
		
		animator = new FPSAnimator(window, FPS, true);
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent _) {
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted()) {
							animator.stop();
						}
						System.exit(0);
					}
				}.start();
			}
		});
		
		window.addGLEventListener(new GLRenderer());
		window.setSize(1280, 720);
		window.setTitle("Dark Forces (c) Lucas Arts - Java");
		window.setVisible(true);
		animator.start();
	}
	
	public synchronized void stop() {
		if (animator != null) {
			animator.stop();
			animator = null;
		}
	}
	
	/** Game main */
	public static void main(String[] args) {
		new GameComponent().run();
	}

}

package util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.Window;

public class BufferHelper {
	private static List<Integer> rbos = new ArrayList<Integer>();
	private static List<Integer> fbos = new ArrayList<Integer>();

	/**
	 * Create an OpenGL FBO
	 * @return		FBO ID
	 */
	private static int generateFBO() {
		int id = glGenFramebuffers();
		fbos.add(id);
		return id;
	}
	
	/**
	 * Create an OpenGL RBO
	 * @return		RBO ID
	 */
	private static int generateRBO() {
		int id = glGenRenderbuffers();
		rbos.add(id);
		return id;
	}
	
	/**
	 * Binds frame buffer
	 * 
	 * @param fbo			FBO ID
	 * @param width			Width of frame
	 * @param height		Height of frame
	 */
	public static void bindFrameBuffer(int fbo, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glViewport(0, 0, width, height);
	}
	
	/**
	 * Unbinds frame buffer
	 * 
	 * @param fbo			OpenGL Frame Buffer ID
	 * @param width			Width of frame
	 * @param height		Height of frame
	 */
	public static void unbindFrameBuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Window.WIDTH, Window.HEIGHT + 100);
	}
	
	/**
	 * Creates a Frame Buffer 
	 * 
	 * @param width			Width of the Frame Buffer
	 * @param height		Height of the Frame Buffer
	 * @param attachments	Number of output channels/attachments
	 * @return				Frame Buffer ID
	 */
	public static int createFrameBuffer(int width, int height, int attachments) {
		int id = generateFBO();
		glBindFramebuffer(GL_FRAMEBUFFER, id);
		int[] buffers = new int[attachments];
		for(int i = 0; i < attachments; i++) {
			buffers[i] = GL_COLOR_ATTACHMENT0 + i;
		}
		glDrawBuffers(buffers);
		createDepthBufferAttachment(width, height);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return id;
	}
	
	/**
	 * Creates a depth buffer attachment
	 * @return			Depth Buffer ID
	 */
	private static int createDepthBufferAttachment(int width, int height) {
		int id = generateRBO();
		glBindRenderbuffer(GL_RENDERBUFFER, id);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, id);
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		return id;
	}
	
	/**
	 * Attaches buffer texture to FBO
	 * 
	 * @paran fbo			OpenGL Frame Buffer ID
	 * @param texture		OpenGL Texture to attach
	 */
	public static void loadBufferTexture(int fbo, Texture texture, int attachment) {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);
		glBindTexture(GL_TEXTURE_2D, texture.getID());
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, (int)texture.getWidth(), (int)texture.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + attachment, texture.getID(), 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	/**
	 * Cleans up all FBOs and RBOs from memory
	 */
	public static void dispose() {
		for(int fbo : fbos) {
			glDeleteFramebuffers(fbo);
		}
		for(int rbo : rbos) {
			glDeleteRenderbuffers(rbo);
		}
	}
}

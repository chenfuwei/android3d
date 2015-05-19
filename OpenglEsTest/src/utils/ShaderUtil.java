package utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

public class ShaderUtil {
	public static void checkGlError(String op) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e("ES20_ERROR", op + ": glError " + error);
			throw new RuntimeException(op + ": glError " + error);
		}
	}

	public static String loadFromAssetsFile(String fname, Resources r) {
		String result = null;
		try {
			InputStream in = r.getAssets().open(fname);
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] buff = baos.toByteArray();
			baos.close();
			in.close();
			result = new String(buff, "UTF-8");
			result = result.replaceAll("\\r\\n", "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 加载指定找色器的方法
	 * 
	 * @param shaderType
	 *            找色器的类型
	 * @param source
	 *            着色器的脚本字符串
	 * @return
	 */
	public static int loadShader(int shaderType, String source) {
		int shader = GLES20.glCreateShader(shaderType); // 创建一个shader,并记录其id
		if (0 != shader) // 创建成功，加载着色器
		{
			GLES20.glShaderSource(shader, source); // 加载着色器的源代码
			GLES20.glCompileShader(shader); // 编译
		}

		int[] compiled = new int[1];
		// 获取shader的编译情况
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
		if (0 == compiled[0]) {// 若编译失败则显示错误日志并删除此shader
			Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
			Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);
			shader = 0;
		}
		return shader;
	}

	// 创建着色器程序的方法
	public static int createProgram(String vertexSouce, String fragmentSource) {
		// 加载顶点着色器
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSouce);
		if (0 == vertexShader) {
			return 0;
		}

		// 加载片元着色器
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if (0 == pixelShader) {
			return 0;
		}

		int program = GLES20.glCreateProgram(); // 创建程序
		if (0 != program) {
			// 若程序创建成功则向程序中加入顶点着色器与片元着色器
			GLES20.glAttachShader(program, vertexShader);// 像程序中加入顶点着色器
			checkGlError("glAttchShader");
			GLES20.glAttachShader(program, pixelShader); // 向程序中加入片元着色器
			checkGlError("glAttchShader");
			GLES20.glLinkProgram(program); // 连接程序
			int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);   //获取连接的状态
			if (GLES20.GL_TRUE != linkStatus[0]) {
				//若连接失败则报错并删除程序
				Log.e("ES20_ERROR", "Could not link program: ");
				Log.e("ES20_ERROR", GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}
}

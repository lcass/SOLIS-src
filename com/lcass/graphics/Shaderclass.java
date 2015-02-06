package com.lcass.graphics;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shaderclass {
	public int programID;
    
    int vertexShaderID;
    int fragmentShaderID;
    int geometryShaderID = -1;
  
    public Shaderclass()
    {
        programID = glCreateProgram();
    }
    
   
    public void attachVertexShader(String name)
    {
        String vertexShaderSource = FileBuilder.gettext(name);
        vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexShaderSource);
        glCompileShader(vertexShaderID);
        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to create vertex shader:");
            dispose();
           
        }
        glAttachShader(programID, vertexShaderID);
    }
  
    public void attachFragmentShader(String name)
    {
        String fragmentShaderSource = FileBuilder.gettext(name);
        fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentShaderSource);
        glCompileShader(fragmentShaderID);
        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to create fragment shader:");
            dispose();
        }
        glAttachShader(programID, fragmentShaderID);
    }
    public void attachGeometryShader(String name)
    {
       
        String geometryShaderSource = FileBuilder.gettext(name);
        geometryShaderID = glCreateShader(GL_GEOMETRY_SHADER);
        glShaderSource(geometryShaderID, geometryShaderSource);

        glCompileShader(geometryShaderID);

        if (glGetShaderi(geometryShaderID, GL_COMPILE_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to create Geometry shader:");
            dispose();

        }

        glAttachShader(programID, geometryShaderID);
    }
    
 
    public void link()
    {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE)
        {
            System.err.println("Unable to link shader program:");
            dispose();
           
        }
    }
  
    public void bind()
    {
        glUseProgram(programID);
    }

    public static void unbind()
    {
        glUseProgram(0);
    }

    public void dispose()
    {
        unbind();
        glDetachShader(programID, vertexShaderID);
        glDetachShader(programID, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        if(geometryShaderID != -1){
        	glDeleteShader(geometryShaderID);
        }
        glDeleteProgram(programID);
    }
}

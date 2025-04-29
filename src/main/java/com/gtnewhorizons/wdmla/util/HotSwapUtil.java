package com.gtnewhorizons.wdmla.util;

import com.gtnewhorizon.gtnhlib.bytebuf.MemoryStack;
import com.gtnewhorizons.wdmla.WDMla;
import org.joml.Matrix4f;
import org.lwjgl.util.glu.GLU;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;

//Intellij Idea HotSwaps lwjgl2 to lwjgl3, we fight it with reflection
public class HotSwapUtil {

    public static void gluLookat(float eyex,
                                 float eyey,
                                 float eyez,
                                 float centerx,
                                 float centery,
                                 float centerz,
                                 float upx,
                                 float upy,
                                 float upz) {
        if(!WDMla.isDevEnv()) {
            GLU.gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);
            return;
        }

        try {
            Method glMultMatrixf = Class.forName("org.lwjgl.opengl.GL11")
                    .getMethod("glMultMatrixf", FloatBuffer.class);
            Matrix4f view = new Matrix4f().lookAt(
                    eyex, eyey, eyez,
                    centerx, centery, centerz,
                    upx, upy, upz
            );
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buf = stack.mallocFloat(16);
                view.get(buf);
                glMultMatrixf.invoke(null, buf);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            GLU.gluLookAt(eyex, eyey, eyez, centerx, centery, centerz, upx, upy, upz);
        }
    }

    public static void gluPerspective(float fovy,
                                      float aspect,
                                      float zNear,
                                      float zFar) {
        if(!WDMla.isDevEnv()) {
            GLU.gluPerspective(fovy, aspect, zNear, zFar);
            return;
        }

        try {
            Method glMultMatrixf = Class.forName("org.lwjgl.opengl.GL11")
                    .getMethod("glMultMatrixf", FloatBuffer.class);
            Matrix4f proj = new Matrix4f().setPerspective(
                    (float) Math.toRadians(fovy), aspect, zNear, zFar
            );
            try (MemoryStack stack = MemoryStack.stackPush()) {
                FloatBuffer buf = stack.mallocFloat(16);
                proj.get(buf);
                glMultMatrixf.invoke(null, buf);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            GLU.gluPerspective(fovy, aspect, zNear, zFar);
        }
    }
}

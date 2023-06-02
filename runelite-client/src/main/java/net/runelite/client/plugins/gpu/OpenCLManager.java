/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  javax.inject.Singleton
 *  net.runelite.rlawt.AWTContext
 *  org.jocl.CL
 *  org.jocl.CLException
 *  org.jocl.NativePointerObject
 *  org.jocl.Pointer
 *  org.jocl.Sizeof
 *  org.jocl.cl_command_queue
 *  org.jocl.cl_context
 *  org.jocl.cl_context_properties
 *  org.jocl.cl_device_id
 *  org.jocl.cl_event
 *  org.jocl.cl_kernel
 *  org.jocl.cl_mem
 *  org.jocl.cl_platform_id
 *  org.jocl.cl_program
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.runelite.client.plugins.gpu;

import com.google.common.base.Charsets;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Singleton;
import net.runelite.client.plugins.gpu.GLBuffer;
import net.runelite.client.plugins.gpu.template.Template;
import net.runelite.client.util.OSType;
import net.runelite.rlawt.AWTContext;
import org.jocl.CL;
import org.jocl.CLException;
import org.jocl.NativePointerObject;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_event;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class OpenCLManager {
    private static final Logger log = LoggerFactory.getLogger(OpenCLManager.class);
    private static final String GL_SHARING_PLATFORM_EXT = "cl_khr_gl_sharing";
    private static final String KERNEL_NAME_UNORDERED = "computeUnordered";
    private static final String KERNEL_NAME_LARGE = "computeLarge";
    private static final int MIN_WORK_GROUP_SIZE = 256;
    private static final int SMALL_SIZE = 512;
    private static final int LARGE_SIZE = 6144;
    private static final int SHARED_SIZE = 43;
    private int largeFaceCount;
    private int smallFaceCount;
    private cl_platform_id platform;
    private cl_device_id device;
    cl_context context;
    private cl_command_queue commandQueue;
    private cl_program programUnordered;
    private cl_program programSmall;
    private cl_program programLarge;
    private cl_kernel kernelUnordered;
    private cl_kernel kernelSmall;
    private cl_kernel kernelLarge;

    OpenCLManager() {
    }

    void init(AWTContext awtContext) {
        CL.setExceptionsEnabled((boolean)true);
        switch (OSType.getOSType()) {
            case Windows: 
            case Linux: {
                this.initPlatform();
                this.initDevice();
                this.initContext(awtContext);
                break;
            }
            case MacOS: {
                this.initMacOS(awtContext);
                break;
            }
            default: {
                throw new RuntimeException("Unsupported OS Type " + OSType.getOSType().name());
            }
        }
        this.ensureMinWorkGroupSize();
        this.initQueue();
        this.compilePrograms();
    }

    void cleanup() {
        if (this.programUnordered != null) {
            CL.clReleaseProgram((cl_program)this.programUnordered);
            this.programUnordered = null;
        }
        if (this.programSmall != null) {
            CL.clReleaseProgram((cl_program)this.programSmall);
            this.programSmall = null;
        }
        if (this.programLarge != null) {
            CL.clReleaseProgram((cl_program)this.programLarge);
            this.programLarge = null;
        }
        if (this.kernelUnordered != null) {
            CL.clReleaseKernel((cl_kernel)this.kernelUnordered);
            this.kernelUnordered = null;
        }
        if (this.kernelSmall != null) {
            CL.clReleaseKernel((cl_kernel)this.kernelSmall);
            this.kernelSmall = null;
        }
        if (this.kernelLarge != null) {
            CL.clReleaseKernel((cl_kernel)this.kernelLarge);
            this.kernelLarge = null;
        }
        if (this.commandQueue != null) {
            CL.clReleaseCommandQueue((cl_command_queue)this.commandQueue);
            this.commandQueue = null;
        }
        if (this.context != null) {
            CL.clReleaseContext((cl_context)this.context);
            this.context = null;
        }
        if (this.device != null) {
            CL.clReleaseDevice((cl_device_id)this.device);
            this.device = null;
        }
    }

    private String logPlatformInfo(cl_platform_id platform, int param) {
        long[] size = new long[1];
        CL.clGetPlatformInfo((cl_platform_id)platform, (int)param, (long)0L, null, (long[])size);
        byte[] buffer = new byte[(int)size[0]];
        CL.clGetPlatformInfo((cl_platform_id)platform, (int)param, (long)buffer.length, (Pointer)Pointer.to((byte[])buffer), null);
        String platformInfo = new String(buffer, Charsets.UTF_8);
        log.debug("Platform: {}, {}", (Object)CL.stringFor_cl_platform_info((int)param), (Object)platformInfo);
        return platformInfo;
    }

    private void logBuildInfo(cl_program program, int param) {
        long[] size = new long[1];
        CL.clGetProgramBuildInfo((cl_program)program, (cl_device_id)this.device, (int)param, (long)0L, null, (long[])size);
        ByteBuffer buffer = ByteBuffer.allocateDirect((int)size[0]);
        CL.clGetProgramBuildInfo((cl_program)program, (cl_device_id)this.device, (int)param, (long)buffer.limit(), (Pointer)Pointer.toBuffer((Buffer)buffer), null);
        switch (param) {
            case 4481: {
                log.debug("Build status: {}, {}", (Object)CL.stringFor_cl_program_build_info((int)param), (Object)CL.stringFor_cl_build_status((int)buffer.getInt()));
                break;
            }
            case 4484: {
                log.debug("Binary type: {}, {}", (Object)CL.stringFor_cl_program_build_info((int)param), (Object)CL.stringFor_cl_program_binary_type((int)buffer.getInt()));
                break;
            }
            case 4483: {
                String buildLog = StandardCharsets.US_ASCII.decode(buffer).toString();
                log.trace("Build log: {}, {}", (Object)CL.stringFor_cl_program_build_info((int)param), (Object)buildLog);
                break;
            }
            case 4482: {
                String message = StandardCharsets.US_ASCII.decode(buffer).toString();
                log.debug("Build options: {}, {}", (Object)CL.stringFor_cl_program_build_info((int)param), (Object)message);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    private void initPlatform() {
        int[] platformCount = new int[1];
        CL.clGetPlatformIDs((int)0, null, (int[])platformCount);
        if (platformCount[0] == 0) {
            throw new RuntimeException("No compute platforms found");
        }
        cl_platform_id[] platforms = new cl_platform_id[platformCount[0]];
        CL.clGetPlatformIDs((int)platforms.length, (cl_platform_id[])platforms, null);
        for (cl_platform_id platform : platforms) {
            log.debug("Found cl_platform_id {}", (Object)platform);
            this.logPlatformInfo(platform, 2304);
            this.logPlatformInfo(platform, 2305);
            this.logPlatformInfo(platform, 2306);
            this.logPlatformInfo(platform, 2307);
            String[] extensions = this.logPlatformInfo(platform, 2308).split(" ");
            if (!Arrays.stream(extensions).anyMatch(s -> s.equals(GL_SHARING_PLATFORM_EXT))) continue;
            this.platform = platform;
        }
        if (this.platform == null) {
            throw new RuntimeException("Platform does not support OpenGL buffer sharing");
        }
        log.debug("Selected cl_platform_id {}", (Object)this.platform);
    }

    private void initDevice() {
        int[] deviceCount = new int[1];
        CL.clGetDeviceIDs((cl_platform_id)this.platform, (long)4L, (int)0, null, (int[])deviceCount);
        if (deviceCount[0] == 0) {
            throw new RuntimeException("No compute devices found");
        }
        cl_device_id[] devices = new cl_device_id[deviceCount[0]];
        CL.clGetDeviceIDs((cl_platform_id)this.platform, (long)4L, (int)devices.length, (cl_device_id[])devices, null);
        for (cl_device_id device : devices) {
            long[] size = new long[1];
            CL.clGetDeviceInfo((cl_device_id)device, (int)4144, (long)0L, null, (long[])size);
            byte[] devInfoBuf = new byte[(int)size[0]];
            CL.clGetDeviceInfo((cl_device_id)device, (int)4144, (long)devInfoBuf.length, (Pointer)Pointer.to((byte[])devInfoBuf), null);
            log.debug("Found cl_device_id: {}", (Object)device);
            log.debug("Device extensions: {}", (Object)new String(devInfoBuf, Charsets.UTF_8));
        }
        this.device = devices[0];
        log.debug("Selected cl_device_id {}", (Object)this.device);
    }

    private void initContext(AWTContext awtContext) {
        cl_context_properties contextProps = new cl_context_properties();
        contextProps.addProperty(4228L, this.platform);
        contextProps.addProperty(8200L, awtContext.getGLContext());
        if (OSType.getOSType() == OSType.Linux) {
            contextProps.addProperty(8202L, awtContext.getGLXDisplay());
        } else if (OSType.getOSType() == OSType.Windows) {
            contextProps.addProperty(8203L, awtContext.getWGLHDC());
        }
        log.debug("Creating context with props: {}", (Object)contextProps);
        this.context = CL.clCreateContext((cl_context_properties)contextProps, (int)1, (cl_device_id[])new cl_device_id[]{this.device}, null, null, null);
        log.debug("Created compute context {}", (Object)this.context);
    }

    private void initMacOS(AWTContext awtContext) {
        long cglContext = awtContext.getGLContext();
        long cglShareGroup = awtContext.getCGLShareGroup();
        log.info("{} {}", (Object)cglContext, (Object)cglShareGroup);
        cl_context_properties contextProps = new cl_context_properties();
        contextProps.addProperty(0x10000000L, cglShareGroup);
        log.debug("Creating context with props: {}", (Object)contextProps);
        this.context = CL.clCreateContext((cl_context_properties)contextProps, (int)0, null, null, null, null);
        this.device = new cl_device_id();
        CL.clGetGLContextInfoAPPLE((cl_context)this.context, (long)cglContext, (int)0x10000002, (long)Sizeof.cl_device_id, (Pointer)Pointer.to((NativePointerObject)this.device), null);
        log.debug("Got macOS CLGL compute device {}", (Object)this.device);
    }

    private void ensureMinWorkGroupSize() {
        long[] maxWorkGroupSize = new long[1];
        CL.clGetDeviceInfo((cl_device_id)this.device, (int)4100, (long)Sizeof.size_t, (Pointer)Pointer.to((long[])maxWorkGroupSize), null);
        log.debug("Device CL_DEVICE_MAX_WORK_GROUP_SIZE: {}", (Object)maxWorkGroupSize[0]);
        if (maxWorkGroupSize[0] < 256L) {
            throw new RuntimeException("Compute device does not support min work group size 256");
        }
        int groupSize = Integer.MIN_VALUE >>> Integer.numberOfLeadingZeros((int)maxWorkGroupSize[0]);
        this.largeFaceCount = 6144 / Math.min(groupSize, 6144);
        this.smallFaceCount = 512 / Math.min(groupSize, 512);
        log.debug("Face counts: small: {}, large: {}", (Object)this.smallFaceCount, (Object)this.largeFaceCount);
    }

    private void initQueue() {
        long[] l = new long[1];
        CL.clGetDeviceInfo((cl_device_id)this.device, (int)4138, (long)8L, (Pointer)Pointer.to((long[])l), null);
        this.commandQueue = CL.clCreateCommandQueue((cl_context)this.context, (cl_device_id)this.device, (long)(l[0] & 1L), null);
        log.debug("Created command_queue {}, properties {}", (Object)this.commandQueue, (Object)(l[0] & 1L));
    }

    private cl_program compileProgram(String programSource) {
        log.trace("Compiling program:\n {}", (Object)programSource);
        cl_program program = CL.clCreateProgramWithSource((cl_context)this.context, (int)1, (String[])new String[]{programSource}, null, null);
        try {
            CL.clBuildProgram((cl_program)program, (int)0, null, null, null, null);
        }
        catch (CLException e) {
            this.logBuildInfo(program, 4483);
            throw e;
        }
        this.logBuildInfo(program, 4481);
        this.logBuildInfo(program, 4484);
        this.logBuildInfo(program, 4482);
        this.logBuildInfo(program, 4483);
        return program;
    }

    private cl_kernel getKernel(cl_program program, String kernelName) {
        cl_kernel kernel = CL.clCreateKernel((cl_program)program, (String)kernelName, null);
        log.debug("Loaded kernel {} for program {}", (Object)kernelName, (Object)program);
        return kernel;
    }

    private void compilePrograms() {
        Template templateSmall = new Template().addInclude(OpenCLManager.class).add(key -> key.equals("FACE_COUNT") ? "#define FACE_COUNT " + this.smallFaceCount : null);
        Template templateLarge = new Template().addInclude(OpenCLManager.class).add(key -> key.equals("FACE_COUNT") ? "#define FACE_COUNT " + this.largeFaceCount : null);
        String unordered = new Template().addInclude(OpenCLManager.class).load("comp_unordered.cl");
        String small = templateSmall.load("comp.cl");
        String large = templateLarge.load("comp.cl");
        this.programUnordered = this.compileProgram(unordered);
        this.programSmall = this.compileProgram(small);
        this.programLarge = this.compileProgram(large);
        this.kernelUnordered = this.getKernel(this.programUnordered, KERNEL_NAME_UNORDERED);
        this.kernelSmall = this.getKernel(this.programSmall, KERNEL_NAME_LARGE);
        this.kernelLarge = this.getKernel(this.programLarge, KERNEL_NAME_LARGE);
    }

    void compute(int unorderedModels, int smallModels, int largeModels, GLBuffer sceneVertexBuffer, GLBuffer sceneUvBuffer, GLBuffer vertexBuffer, GLBuffer uvBuffer, GLBuffer unorderedBuffer, GLBuffer smallBuffer, GLBuffer largeBuffer, GLBuffer outVertexBuffer, GLBuffer outUvBuffer, GLBuffer uniformBuffer) {
        cl_mem[] glBuffersAll = new cl_mem[]{sceneVertexBuffer.cl_mem, sceneUvBuffer.cl_mem, unorderedBuffer.cl_mem, smallBuffer.cl_mem, largeBuffer.cl_mem, vertexBuffer.cl_mem, uvBuffer.cl_mem, outVertexBuffer.cl_mem, outUvBuffer.cl_mem, uniformBuffer.cl_mem};
        cl_mem[] glBuffers = (cl_mem[])Arrays.stream(glBuffersAll).filter(Objects::nonNull).toArray(cl_mem[]::new);
        cl_event acquireGLBuffers = new cl_event();
        CL.clEnqueueAcquireGLObjects((cl_command_queue)this.commandQueue, (int)glBuffers.length, (cl_mem[])glBuffers, (int)0, null, (cl_event)acquireGLBuffers);
        cl_event[] computeEvents = new cl_event[]{new cl_event(), new cl_event(), new cl_event()};
        int numComputeEvents = 0;
        if (unorderedModels > 0) {
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)0, (long)Sizeof.cl_mem, (Pointer)unorderedBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)1, (long)Sizeof.cl_mem, (Pointer)sceneVertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)2, (long)Sizeof.cl_mem, (Pointer)vertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)3, (long)Sizeof.cl_mem, (Pointer)sceneUvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)4, (long)Sizeof.cl_mem, (Pointer)uvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)5, (long)Sizeof.cl_mem, (Pointer)outVertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelUnordered, (int)6, (long)Sizeof.cl_mem, (Pointer)outUvBuffer.ptr());
            CL.clEnqueueNDRangeKernel((cl_command_queue)this.commandQueue, (cl_kernel)this.kernelUnordered, (int)1, null, (long[])new long[]{(long)unorderedModels * 6L}, (long[])new long[]{6L}, (int)1, (cl_event[])new cl_event[]{acquireGLBuffers}, (cl_event)computeEvents[numComputeEvents++]);
        }
        if (smallModels > 0) {
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)0, (long)2220L, null);
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)1, (long)Sizeof.cl_mem, (Pointer)smallBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)2, (long)Sizeof.cl_mem, (Pointer)sceneVertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)3, (long)Sizeof.cl_mem, (Pointer)vertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)4, (long)Sizeof.cl_mem, (Pointer)sceneUvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)5, (long)Sizeof.cl_mem, (Pointer)uvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)6, (long)Sizeof.cl_mem, (Pointer)outVertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)7, (long)Sizeof.cl_mem, (Pointer)outUvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelSmall, (int)8, (long)Sizeof.cl_mem, (Pointer)uniformBuffer.ptr());
            CL.clEnqueueNDRangeKernel((cl_command_queue)this.commandQueue, (cl_kernel)this.kernelSmall, (int)1, null, (long[])new long[]{smallModels * (512 / this.smallFaceCount)}, (long[])new long[]{512 / this.smallFaceCount}, (int)1, (cl_event[])new cl_event[]{acquireGLBuffers}, (cl_event)computeEvents[numComputeEvents++]);
        }
        if (largeModels > 0) {
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)0, (long)24748L, null);
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)1, (long)Sizeof.cl_mem, (Pointer)largeBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)2, (long)Sizeof.cl_mem, (Pointer)sceneVertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)3, (long)Sizeof.cl_mem, (Pointer)vertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)4, (long)Sizeof.cl_mem, (Pointer)sceneUvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)5, (long)Sizeof.cl_mem, (Pointer)uvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)6, (long)Sizeof.cl_mem, (Pointer)outVertexBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)7, (long)Sizeof.cl_mem, (Pointer)outUvBuffer.ptr());
            CL.clSetKernelArg((cl_kernel)this.kernelLarge, (int)8, (long)Sizeof.cl_mem, (Pointer)uniformBuffer.ptr());
            CL.clEnqueueNDRangeKernel((cl_command_queue)this.commandQueue, (cl_kernel)this.kernelLarge, (int)1, null, (long[])new long[]{(long)largeModels * (long)(6144 / this.largeFaceCount)}, (long[])new long[]{6144 / this.largeFaceCount}, (int)1, (cl_event[])new cl_event[]{acquireGLBuffers}, (cl_event)computeEvents[numComputeEvents++]);
        }
        if (numComputeEvents == 0) {
            CL.clEnqueueReleaseGLObjects((cl_command_queue)this.commandQueue, (int)glBuffers.length, (cl_mem[])glBuffers, (int)0, null, null);
        } else {
            CL.clEnqueueReleaseGLObjects((cl_command_queue)this.commandQueue, (int)glBuffers.length, (cl_mem[])glBuffers, (int)numComputeEvents, (cl_event[])computeEvents, null);
        }
    }

    void finish() {
        CL.clFinish((cl_command_queue)this.commandQueue);
    }
}


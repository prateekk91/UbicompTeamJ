/**
 * 
 */
package Skeleton;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;

/**
 * @author Prateek
 *
 */
public class SkeletonTracker extends J4KSDK
{
	public static void main(String ab[]) {
		SkeletonTracker myKinect = new SkeletonTracker(); 
		if(!myKinect.start(J4KSDK.DEPTH|J4KSDK.SKELETON|J4KSDK.COLOR|J4KSDK.XYZ|J4KSDK.UV|J4KSDK.PLAYER_INDEX))
		{
			System.out.println("Could not start");
			//System.exit(0); 
		}
	}

	/* (non-Javadoc)
	 * @see edu.ufl.digitalworlds.j4k.J4KSDK#onColorFrameEvent(byte[])
	 */
	@Override
	public void onColorFrameEvent(byte[] arg0)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.ufl.digitalworlds.j4k.J4KSDK#onDepthFrameEvent(short[], byte[], float[], float[])
	 */
	@Override
	public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see edu.ufl.digitalworlds.j4k.J4KSDK#onSkeletonFrameEvent(boolean[], float[], float[], byte[])
	 */
	@Override 
    public void onSkeletonFrameEvent(boolean[] skeleton_tracked, float[] joint_position, float[] joint_orientation, byte[] joint_status) { 

        Skeleton skeletons[]=new Skeleton[getMaxNumberOfSkeletons()]; 
        for(int i=0;i<getMaxNumberOfSkeletons();i++) 
          skeletons[i]=Skeleton.getSkeleton(i, skeleton_tracked, joint_position, joint_orientation, joint_status, this);
        float jointPositions[] = skeletons[0].getJointPositions();
        for (int i=0;i<jointPositions.length;i++) {
        	System.out.print(jointPositions[i] + " ");
        }
    } 
	
}

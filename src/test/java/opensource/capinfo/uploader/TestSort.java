package opensource.capinfo.uploader;

import java.util.Arrays;

public class TestSort {

    public static void main(String[] args) {
        int[] arr = {2,8,13,11,6,7};
       // bubbleSort(arr);
        selectSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void bubbleSort(int[] arr){
        for(int i=0; i<arr.length; i++){
            for(int j=0; j<arr.length; j++){
                if(arr[i]>arr[j]){
                    int temp = arr[i];
                    arr[i]=arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
    public static void selectSort( int[] arr){
        for(int i=0; i<arr.length; i++){
            int lowerIndex  = i;
            for(int j=lowerIndex+1; j<arr.length; j++){
                if(arr[j]<arr[lowerIndex]){
                    lowerIndex = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[lowerIndex];
            arr[lowerIndex] = temp;
        }
    }
}

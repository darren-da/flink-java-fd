package com.alibaba.test;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/18 13:55
 * @Desc:
 */
public class TestGetData {
    public static void main(String[] args) {
        //source: [{
        //    pathDesc: '2.2.1',
        //            propertyPath: 'freeDetail001.freeDetail002.productId_name',
        //},{
        //    pathDesc: '1',
        //            propertyPath: 'productId_name',
        //},{
        //    pathDesc: '2.3.4',
        //            propertyPath: 'freeDetail001.p1freeCharacteristics.TEST_USER_30',
        //},{
        //    pathDesc: '2.2.3.4',
        //            propertyPath: 'freeDetail001.freeDetail002.p1freeCharacteristics.TEST_USER_30',
        //},{
        //    pathDesc: '2.1',
        //            propertyPath: 'freeDetail001.productId_name',
        //},{
        //    pathDesc: '3.4',
        //            propertyPath: 'freeDetail001.productId_name',
        //}]


        //2.2.1  modelData = model.getRowModel(model.getFocusedRowIndex()).get('p1freeCharacteristics')

        //modelData=modelData?.get(propertyPath[i]) 是2
        //modelData?.getRowModel(modelData.getFocusedRowIndex()) 上一位是2，下一位不是2

        String[] pathDesc= "2.3.4".split("\\.");
        String[] propertyPath = "freeDetail001.p1freeCharacteristics.TEST_USER_30".split("\\.");

        //modelData?.get(freeDetail001).getRowModel(modelData.getFocusedRowIndex()).get(p1freeCharacteristics).get(TEST_USER_30)

        String modelData="";
        for (int i = 0; i <pathDesc.length ; i++) {
            if (i==0){
                //modelData="modelData?.get(propertyPath[i])";
                modelData="modelData?.get("+propertyPath[i]+")";
            }else {
                if (pathDesc[i-1].equals("2") && !pathDesc[i].equals("2")){
                    //modelData="modelData?.getRowModel(modelData.getFocusedRowIndex()).get(propertyPath[i])";
                    modelData="modelData?.getRowModel(modelData.getFocusedRowIndex()).get("+propertyPath[i]+")";
                }else  {
                    //modelData="modelData?.get(propertyPath[i])";
                    modelData="modelData?.get("+propertyPath[i]+")";

                }
            }
            System.out.println(modelData);
        }

    }
}
//const propertyPath = item?.propertyPath?.split('.')
//        const pathDesc = item?.pathDesc?.split('.')
//        for(let i = 0; i　< pathDesc.length; i++) {
//            if(i==0){
//        modelData = modelData?.get(propertyPath[i])
//        }else{
//            if (pathDesc[i-1]==2 && pathDesc[i]!=2) {
//            modelData = modelData?.getRowModel(modelData.getFocusedRowIndex())
//            } else {
//            modelData = modelData?.get(propertyPath[i])
//            }
//        }
//        }
package lam.fooapp.model;

public class OrderForm {
    Integer orderNo;
    Integer tableNo;
    Integer[] foodSerialList;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public Integer[] getFoodSerialList() {
        return foodSerialList;
    }

    public void setFoodSerialList(Integer[] foodSerialList) {
        this.foodSerialList = foodSerialList;
    }
}

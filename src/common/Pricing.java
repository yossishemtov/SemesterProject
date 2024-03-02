package common;

public class Pricing {
	private Integer discount;
	private String typeOfVisit;
	
	public Pricing(Integer discount, String typeOfVisit) {
		this.discount = discount;
		this.typeOfVisit = typeOfVisit;
	}
	
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	public String getTypeOfVisit() {
		return typeOfVisit;
	}
	public void setTypeOfVisit(String typeOfVisit) {
		this.typeOfVisit = typeOfVisit;
	}
	
	
}

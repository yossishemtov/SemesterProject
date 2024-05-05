package common;

/**
 * The Pricing class represents pricing information for different types of visits.
 * It contains attributes such as discount and type of visit.
 */
public class Pricing {
	private Integer discount;
	private String typeOfVisit;
	
	/**
     * Constructs a new Pricing object with the specified discount and type of visit.
     *
     * @param discount     The discount percentage applied to the pricing.
     * @param typeOfVisit  The type of visit (e.g., solo, family, guided group).
     */
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

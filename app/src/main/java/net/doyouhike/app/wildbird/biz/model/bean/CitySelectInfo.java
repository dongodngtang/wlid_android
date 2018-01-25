package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 设置城市对话框点击“完成”之后要使用到的类， 主要是为了往Activity回传数据
 */
public class CitySelectInfo {

	private int provinceId=0;
	private String provinceName;
	private int cityId;
	private String cityName;
	
	public CitySelectInfo(int provinceId, String provinceName,
						  int cityId, String cityName) {
		this.provinceId = provinceId;
		this.provinceName = provinceName;
		this.cityId = cityId;
		this.cityName = cityName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CitySelectInfo that = (CitySelectInfo) o;

		if (provinceId != that.provinceId) return false;
		return cityId == that.cityId;

	}

	@Override
	public int hashCode() {
		int result = provinceId;
		result = 31 * result + cityId;
		return result;
	}
}

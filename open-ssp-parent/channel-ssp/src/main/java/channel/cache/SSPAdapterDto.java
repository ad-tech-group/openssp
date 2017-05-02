package channel.cache;

import java.io.Serializable;
import java.util.List;

/**
 * @author André Schmer
 *
 */
public class SSPAdapterDto implements Serializable {

	private static final long serialVersionUID = -8474260463798948583L;

	private List<BasicAdapter> data;

	public SSPAdapterDto() {}

	public List<BasicAdapter> getData() {
		return data;
	}

	public void setData(final List<BasicAdapter> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("SSPAdapterDto [data=%s]", data);
	}

}

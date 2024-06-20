
package acme.util;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class BooleanLocalizeHelper {

	private static final Map<String, Function<Boolean, String>> localization = Map.ofEntries(//
		Map.entry("es", x -> x ? "Sí" : "No"), //
		Map.entry("en", x -> x ? "Yes" : "No"));


	public static String localizeBoolean(final Locale locale, final boolean value) {
		return BooleanLocalizeHelper.localization//
			.getOrDefault(locale.getLanguage(), x -> x ? "Yes" : "No")//
			.apply(value);
	}
}

package fr.coco5843.ctos.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.base.Joiner;

/**
 * <h1>Skyoconfig</h1>
 * <p><i>Handle configurations with ease !</i></p>
 * <p><b>Current version :</b> v0.4.2.
 * 
 * @author <b>Skyost</b> (<a href="http://www.skyost.eu">www.skyost.eu</a>).
 * <br>Inspired from <a href="https://forums.bukkit.org/threads/lib-supereasyconfig-v1-2-based-off-of-codename_bs-awesome-easyconfig-v2-1.100569/">SuperEasyConfig</a>.</br>
 */

public class Skyoconfig {
	
	private static final transient char DEFAULT_SEPARATOR = '_';
	private static final transient String LINE_SEPARATOR = System.lineSeparator();
	private static final transient String TEMP_CONFIG_SECTION = "temp";
	public static final transient HashMap<Class<?>, Class<?>> PRIMITIVES_CLASS = new HashMap<Class<?>, Class<?>>() {
		private static final long serialVersionUID = 1L; {
			put(int.class, Integer.class);
			put(long.class, Long.class);
			put(double.class, Double.class);
			put(float.class, Float.class);
			put(boolean.class, Boolean.class);
			put(byte.class, Byte.class);
			put(void.class, Void.class);
			put(short.class, Short.class);
		}
	};
	
	private transient File configFile;
	private transient List<String> header;
	
	/**
	 * Creates a new instance of Skyoconfig without header.
	 * 
	 * @param configFile The file where the configuration will be loaded an saved.
	 */
	
	protected Skyoconfig(final File configFile) {
		this.configFile = configFile;
	}
	
	/**
	 * Creates a new instance of Skyoconfig.
	 * 
	 * @param configFile The file where the configuration will be loaded an saved.
	 * @param header The configuration's header.
	 */
	
	protected Skyoconfig(final File configFile, final List<String> header) {
		this.configFile = configFile;
		this.header = header;
	}
	
	/**
	 * Loads the configuration from the specified file.
	 * 
	 * @throws InvalidConfigurationException If there is an error while loading the config.
	 */
	
	public final void load() throws InvalidConfigurationException {
		try {
			final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			for(final Field field : getClass().getFields()) {
				loadField(field, getFieldName(field), config);
			}
			saveConfig(config);
		}
		catch(final Exception ex) {
			throw new InvalidConfigurationException(ex);
		}
	}
	
	/**
	 * Saves the configuration to the specified file.
	 * 
	 * @throws InvalidConfigurationException If there is an error while saving the config.
	 */
	
	public final void save() throws InvalidConfigurationException {
		try {
			final YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			for(final Field field : getClass().getFields()) {
				saveField(field, getFieldName(field), config);
			}
			saveConfig(config);
		}
		catch(final Exception ex) {
			throw new InvalidConfigurationException(ex);
		}
	}
	
	/**
	 * Gets the formatted <b>Field</b>'s name.
	 * 
	 * @param field The <b>Field</b>.
	 * 
	 * @return The formatted <b>Field</b>'s name.
	 */
	
	private final String getFieldName(final Field field) {
		final ConfigOptions options = field.getAnnotation(ConfigOptions.class);
		return (options == null ? field.getName().replace(DEFAULT_SEPARATOR, '.') : options.name());
	}
	
	/**
	 * Saves the configuration.
	 * 
	 * @param config The <b>YamlConfiguration</b>.
	 * 
	 * @throws IOException <b>InputOutputException</b>.
	 */
	
	private final void saveConfig(final YamlConfiguration config) throws IOException {
		if(header != null && header.size() > 0) {
			config.options().header(Joiner.on(LINE_SEPARATOR).join(header));
		}
		config.save(configFile);
	}
	
	/**
	 * Loads a Field from its path from the config.
	 * 
	 * @param field The specified <b>Field</b>.
	 * @param name The <b>Field</b>'s name. Will be the path.
	 * @param config The <b>YamlConfiguration</b>.
	 * 
	 * @throws ParseException If the JSON parser fails to parse a <b>Location</b> or a <b>Vector</b>.
	 * @throws IllegalAccessException If <b>Skyoconfig</b> does not have access to the <b>Field</b> or the <b>Method</b> <b>valueOf</b> of a <b>Primitive</b>.
	 * @throws InvocationTargetException Invoked if the <b>Skyoconfig</b> fails to use <b>valueOf</b> for a <b>Primitive</b>.
	 * @throws NoSuchMethodException Same as <b>InvocationTargetException</b>.
	 */
	
	private final void loadField(final Field field, final String name, final YamlConfiguration config) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException {
		if(Modifier.isTransient(field.getModifiers())) {
			return;
		}
		final Object configValue = config.get(getFieldName(field));
		if(configValue == null) {
			saveField(field, name, config);
		}
		else {
			field.set(this, deserializeObject(field.getType(), configValue));
		}
	}
	
	/**
	 * Saves a <b>Field</b> to the config.
	 * 
	 * @param field The specified <b>Field</b>.
	 * @param name The <b>Field</b>'s name. The path of the value in the config.
	 * @param config The <b>YamlConfiguration</b>.
	 * 
	 * @throws IllegalAccessException If <b>Skyoconfig</b> does not have access to the <b>Field</b>.
	 */
	
	private final void saveField(final Field field, final String name, final YamlConfiguration config) throws IllegalAccessException {
		if(Modifier.isTransient(field.getModifiers())) {
			return;
		}
		config.set(name, serializeObject(field.get(this), config));
	}
	
	/**
	 * Deserializes an <b>Object</b> from the configuration.
	 * 
	 * @param clazz The object's <b>Type</b>.
	 * @param object The <b>Object</b>'s.
	 * 
	 * @return The deserialized value of the specified <b>Object</b>.
	 * 
	 * @throws ParseException If the JSON parser fails to parse a <b>Location</b> or a <b>Vector</b>.
	 * @throws IllegalAccessException If <b>Skyoconfig</b> does not have access to the <b>Field</b> or the <b>Method</b> <b>valueOf</b> of a <b>Primitive</b>.
	 * @throws InvocationTargetException Invoked if the <b>Skyoconfig</b> fails to use <b>valueOf</b> for a <b>Primitive</b>.
	 * @throws NoSuchMethodException Same as <b>InvocationTargetException</b>.
	 */
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	private final Object deserializeObject(final Class<?> clazz, final Object object) throws ParseException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(PRIMITIVES_CLASS.containsValue(clazz) || clazz.isPrimitive()) {
			return PRIMITIVES_CLASS.get(clazz).getMethod("valueOf", String.class).invoke(this, object.toString());
		}
		if(clazz.isEnum() || object instanceof Enum<?>) {
			return Enum.valueOf((Class<? extends Enum>)clazz, object.toString());
		}
		if(Map.class.isAssignableFrom(clazz) || object instanceof Map) {
			final ConfigurationSection section = (ConfigurationSection)object;
			final Map<Object, Object> map = new HashMap<Object, Object>();
			for(final String key : section.getKeys(false)) {
				final Object value = section.get(key);
				map.put(key, deserializeObject(value.getClass(), value));
			}
			return map;
		}
		if(List.class.isAssignableFrom(clazz) || object instanceof List) {
			final List<Object> result = new ArrayList<Object>();
			for(final Object value : (List<?>)object) {
				result.add(deserializeObject(value.getClass(), value));
			}
			return result;
		}
		if(Location.class.isAssignableFrom(clazz) || object instanceof Location) {
			final JSONObject jsonObject = (JSONObject)new JSONParser().parse(object.toString());
			return new Location(Bukkit.getWorld(jsonObject.get("world").toString()), Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()), Double.parseDouble(jsonObject.get("z").toString()), Float.parseFloat(jsonObject.get("yaw").toString()), Float.parseFloat(jsonObject.get("pitch").toString()));
		}
		if(Vector.class.isAssignableFrom(clazz) || object instanceof Vector) {
			final JSONObject jsonObject = (JSONObject)new JSONParser().parse(object.toString());
			return new Vector(Double.parseDouble(jsonObject.get("x").toString()), Double.parseDouble(jsonObject.get("y").toString()), Double.parseDouble(jsonObject.get("z").toString()));
		}
		return object.toString();
	}
	
	/**
	 * Serializes an <b>Object</b> to the configuration.
	 * 
	 * @param object The specified <b>Object</b>.
	 * @param config The <b>YamlConfiguration</b>. Used to temporally save <b>Map</b>s.
	 * 
	 * @return The serialized <b>Object</b>.
	 */
	
	@SuppressWarnings("unchecked")
	private final Object serializeObject(final Object object, final YamlConfiguration config) {
		if(object instanceof Enum) {
			return ((Enum<?>)object).name();
		}
		if(object instanceof Map) {
			final ConfigurationSection section = config.createSection(TEMP_CONFIG_SECTION);
			for(final Entry<?, ?> entry : ((Map<?, ?>)object).entrySet()) {
				section.set(entry.getKey().toString(), serializeObject(entry.getValue(), config));
			}
			config.set(TEMP_CONFIG_SECTION, null);
			return section;
		}
		if(object instanceof List) {
			final List<Object> result = new ArrayList<Object>();
			for(final Object value : (List<?>)object) {
				result.add(serializeObject(value, config));
			}
			return result;
		}
		if(object instanceof Location) {
			final Location location = (Location)object;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("x", location.getX());
			jsonObject.put("y", location.getY());
			jsonObject.put("z", location.getZ());
			jsonObject.put("yaw", location.getYaw());
			jsonObject.put("pitch", location.getPitch());
			return jsonObject.toJSONString();
		}
		if(object instanceof Vector) {
			final Vector vector = (Vector)object;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put("x", vector.getX());
			jsonObject.put("y", vector.getY());
			jsonObject.put("z", vector.getZ());
			return jsonObject.toJSONString();
		}
		return object.toString();
	}
	
	/**
	 * Gets the configuration's header.
	 * 
	 * @return The header.
	 */
	
	public final List<String> getHeader() {
		return header;
	}
	
	/**
	 * Gets the configuration's <b>File</b>.
	 * 
	 * @return The <b>File</b>.
	 */
	
	public final File getFile() {
		return configFile;
	}
	
	/**
	 * Sets the configuration's header.
	 * 
	 * @param header The header.
	 */
	
	public final void setHeader(final List<String> header) {
		this.header = header;
	}
	
	/**
	 * Sets the configuration's <b>File</b>.
	 * 
	 * @param configFile The <b>File</b>.
	 */
	
	public final void setFile(final File configFile) {
		this.configFile = configFile;
	}
	
	/**
	 * Extra params for configuration fields.
	 */
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	protected @interface ConfigOptions {
		
		/**
		 * The key's name.
		 * 
		 * @return The key's name.
		 */
		
		public String name();
		
	}
	
}
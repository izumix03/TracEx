package iz.tracex.dto.trac.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CopyUtil {

	public static Object deepCopy(Object obj){
		try{
			Class<? extends Object> clazz = obj.getClass();
			Object clone = clazz.newInstance();
			
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields){
				f.setAccessible(true);
				
				if(!Modifier.isFinal(f.getModifiers())){
					 if (f.getType().isPrimitive()) {
                         f.set(clone, f.get(obj));
                     } else {
                         f.set(clone, deepCopyObject(f.get(obj)));
                     }
				}
			}
			// 渡されたオブジェクトの親クラスをさかのぼる
			while( true ) {
				clazz = clazz.getSuperclass();
				// Objectクラスまで到達したら終了
				if ( Object.class.equals(clazz)) break;
				Field[] sFields = clazz.getDeclaredFields();
				for (Field f: sFields) {
					// アクセス制御属性がfinalでなければ値をset
					if ( ! Modifier.isFinal(f.getModifiers())) {
						f.setAccessible(true);
						f.set(clone, f.get(obj));
					}
				}
			}
			return clone;
		} catch (Exception e) {
			return null;
        }
    }

	protected static Object deepCopyObject(Object obj) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Object ret = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            ret = ois.readObject();
        } catch (Exception e) {
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
            }
        }

        return ret;
    }
}

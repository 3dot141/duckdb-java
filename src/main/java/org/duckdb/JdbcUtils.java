package org.duckdb;

import org.duckdb.call.DuckDBJavaCall;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

final class JdbcUtils {

    @SuppressWarnings("unchecked")
    static <T> T unwrap(Object obj, Class<T> iface) throws SQLException {
        if (!iface.isInstance(obj)) {
            throw new SQLException(obj.getClass().getName() + " not unwrappable from " + iface.getName());
        }
        return (T) obj;
    }

    static byte[] readAllBytes(InputStream x) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] thing = new byte[256];
        int length;
        int offset = 0;
        while ((length = x.read(thing)) != -1) {
            out.write(thing, offset, length);
            offset += length;
        }
        return out.toByteArray();
    }

    public static String getSignature(Method m) {

        String sig;
        try {
            Field gSig = Method.class.getDeclaredField("signature");
            gSig.setAccessible(true);
            sig = (String) gSig.get(m);
            if (sig != null) return sig;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder("(");
        for (Class<?> c : m.getParameterTypes())
            sb.append((sig = java.lang.reflect.Array.newInstance(c, 0).toString())
                    .substring(1, sig.indexOf('@')));

        return sb.append(')')
                .append(
                        m.getReturnType() == void.class ? "V" :
                                (sig = java.lang.reflect.Array.newInstance(m.getReturnType(), 0).toString()).substring(1, sig.indexOf('@'))
                )
                .toString()
                .replaceAll("\\.", "/");
    }

    private JdbcUtils() {
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = DuckDBJavaCall.class.getMethod("toDate", long.class);
        String signature = getSignature(method);
        System.out.println();
    }

}

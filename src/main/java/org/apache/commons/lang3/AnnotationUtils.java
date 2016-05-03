package org.apache.commons.lang3;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnnotationUtils {

    private static final ToStringStyle TO_STRING_STYLE = new ToStringStyle() {
        private static final long serialVersionUID = 1L;

        {
            this.setDefaultFullDetail(true);
            this.setArrayContentDetail(true);
            this.setUseClassName(true);
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            this.setContentStart("(");
            this.setContentEnd(")");
            this.setFieldSeparator(", ");
            this.setArrayStart("[");
            this.setArrayEnd("]");
        }

        protected String getShortClassName(Class oclass) {
            Class oclass1 = null;
            Iterator iterator = ClassUtils.getAllInterfaces(oclass).iterator();

            while (iterator.hasNext()) {
                Class oclass2 = (Class) iterator.next();

                if (Annotation.class.isAssignableFrom(oclass2)) {
                    oclass1 = oclass2;
                    break;
                }
            }

            return (new StringBuilder(oclass1 == null ? "" : oclass1.getName())).insert(0, '@').toString();
        }

        protected void appendDetail(StringBuffer stringbuffer, String s, Object object) {
            if (object instanceof Annotation) {
                object = AnnotationUtils.toString((Annotation) object);
            }

            super.appendDetail(stringbuffer, s, object);
        }
    };

    public static boolean equals(Annotation annotation, Annotation annotation1) {
        if (annotation == annotation1) {
            return true;
        } else if (annotation != null && annotation1 != null) {
            Class oclass = annotation.annotationType();
            Class oclass1 = annotation1.annotationType();

            Validate.notNull(oclass, "Annotation %s with null annotationType()", new Object[] { annotation});
            Validate.notNull(oclass1, "Annotation %s with null annotationType()", new Object[] { annotation1});
            if (!oclass.equals(oclass1)) {
                return false;
            } else {
                try {
                    Method[] amethod = oclass.getDeclaredMethods();
                    int i = amethod.length;

                    for (int j = 0; j < i; ++j) {
                        Method method = amethod[j];

                        if (method.getParameterTypes().length == 0 && isValidAnnotationMemberType(method.getReturnType())) {
                            Object object = method.invoke(annotation, new Object[0]);
                            Object object1 = method.invoke(annotation1, new Object[0]);

                            if (!memberEquals(method.getReturnType(), object, object1)) {
                                return false;
                            }
                        }
                    }

                    return true;
                } catch (IllegalAccessException illegalaccessexception) {
                    return false;
                } catch (InvocationTargetException invocationtargetexception) {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static int hashCode(Annotation annotation) {
        int i = 0;
        Class oclass = annotation.annotationType();
        Method[] amethod = oclass.getDeclaredMethods();
        int j = amethod.length;

        for (int k = 0; k < j; ++k) {
            Method method = amethod[k];

            try {
                Object object = method.invoke(annotation, new Object[0]);

                if (object == null) {
                    throw new IllegalStateException(String.format("Annotation method %s returned null", new Object[] { method}));
                }

                i += hashMember(method.getName(), object);
            } catch (RuntimeException runtimeexception) {
                throw runtimeexception;
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }

        return i;
    }

    public static String toString(Annotation annotation) {
        ToStringBuilder tostringbuilder = new ToStringBuilder(annotation, AnnotationUtils.TO_STRING_STYLE);
        Method[] amethod = annotation.annotationType().getDeclaredMethods();
        int i = amethod.length;

        for (int j = 0; j < i; ++j) {
            Method method = amethod[j];

            if (method.getParameterTypes().length <= 0) {
                try {
                    tostringbuilder.append(method.getName(), method.invoke(annotation, new Object[0]));
                } catch (RuntimeException runtimeexception) {
                    throw runtimeexception;
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            }
        }

        return tostringbuilder.build();
    }

    public static boolean isValidAnnotationMemberType(Class oclass) {
        if (oclass == null) {
            return false;
        } else {
            if (oclass.isArray()) {
                oclass = oclass.getComponentType();
            }

            return oclass.isPrimitive() || oclass.isEnum() || oclass.isAnnotation() || String.class.equals(oclass) || Class.class.equals(oclass);
        }
    }

    private static int hashMember(String s, Object object) {
        int i = s.hashCode() * 127;

        return object.getClass().isArray() ? i ^ arrayMemberHash(object.getClass().getComponentType(), object) : (object instanceof Annotation ? i ^ hashCode((Annotation) object) : i ^ object.hashCode());
    }

    private static boolean memberEquals(Class oclass, Object object, Object object1) {
        return object == object1 ? true : (object != null && object1 != null ? (oclass.isArray() ? arrayMemberEquals(oclass.getComponentType(), object, object1) : (oclass.isAnnotation() ? equals((Annotation) object, (Annotation) object1) : object.equals(object1))) : false);
    }

    private static boolean arrayMemberEquals(Class oclass, Object object, Object object1) {
        return oclass.isAnnotation() ? annotationArrayMemberEquals((Annotation[]) ((Annotation[]) object), (Annotation[]) ((Annotation[]) object1)) : (oclass.equals(Byte.TYPE) ? Arrays.equals((byte[]) ((byte[]) object), (byte[]) ((byte[]) object1)) : (oclass.equals(Short.TYPE) ? Arrays.equals((short[]) ((short[]) object), (short[]) ((short[]) object1)) : (oclass.equals(Integer.TYPE) ? Arrays.equals((int[]) ((int[]) object), (int[]) ((int[]) object1)) : (oclass.equals(Character.TYPE) ? Arrays.equals((char[]) ((char[]) object), (char[]) ((char[]) object1)) : (oclass.equals(Long.TYPE) ? Arrays.equals((long[]) ((long[]) object), (long[]) ((long[]) object1)) : (oclass.equals(Float.TYPE) ? Arrays.equals((float[]) ((float[]) object), (float[]) ((float[]) object1)) : (oclass.equals(Double.TYPE) ? Arrays.equals((double[]) ((double[]) object), (double[]) ((double[]) object1)) : (oclass.equals(Boolean.TYPE) ? Arrays.equals((boolean[]) ((boolean[]) object), (boolean[]) ((boolean[]) object1)) : Arrays.equals((Object[]) ((Object[]) object), (Object[]) ((Object[]) object1))))))))));
    }

    private static boolean annotationArrayMemberEquals(Annotation[] aannotation, Annotation[] aannotation1) {
        if (aannotation.length != aannotation1.length) {
            return false;
        } else {
            for (int i = 0; i < aannotation.length; ++i) {
                if (!equals(aannotation[i], aannotation1[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    private static int arrayMemberHash(Class oclass, Object object) {
        return oclass.equals(Byte.TYPE) ? Arrays.hashCode((byte[]) ((byte[]) object)) : (oclass.equals(Short.TYPE) ? Arrays.hashCode((short[]) ((short[]) object)) : (oclass.equals(Integer.TYPE) ? Arrays.hashCode((int[]) ((int[]) object)) : (oclass.equals(Character.TYPE) ? Arrays.hashCode((char[]) ((char[]) object)) : (oclass.equals(Long.TYPE) ? Arrays.hashCode((long[]) ((long[]) object)) : (oclass.equals(Float.TYPE) ? Arrays.hashCode((float[]) ((float[]) object)) : (oclass.equals(Double.TYPE) ? Arrays.hashCode((double[]) ((double[]) object)) : (oclass.equals(Boolean.TYPE) ? Arrays.hashCode((boolean[]) ((boolean[]) object)) : Arrays.hashCode((Object[]) ((Object[]) object)))))))));
    }
}

package org.apache.commons.lang3.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.Builder;

public class TypeUtils {

    public static final WildcardType WILDCARD_ALL = wildcardType().withUpperBounds(new Type[] { Object.class}).build();

    public static boolean isAssignable(Type type, Type type1) {
        return isAssignable(type, type1, (Map) null);
    }

    private static boolean isAssignable(Type type, Type type1, Map map) {
        if (type1 != null && !(type1 instanceof Class)) {
            if (type1 instanceof ParameterizedType) {
                return isAssignable(type, (ParameterizedType) type1, map);
            } else if (type1 instanceof GenericArrayType) {
                return isAssignable(type, (GenericArrayType) type1, map);
            } else if (type1 instanceof WildcardType) {
                return isAssignable(type, (WildcardType) type1, map);
            } else if (type1 instanceof TypeVariable) {
                return isAssignable(type, (TypeVariable) type1, map);
            } else {
                throw new IllegalStateException("found an unhandled type: " + type1);
            }
        } else {
            return isAssignable(type, (Class) type1);
        }
    }

    private static boolean isAssignable(Type type, Class oclass) {
        if (type == null) {
            return oclass == null || !oclass.isPrimitive();
        } else if (oclass == null) {
            return false;
        } else if (oclass.equals(type)) {
            return true;
        } else if (type instanceof Class) {
            return ClassUtils.isAssignable((Class) type, oclass);
        } else if (type instanceof ParameterizedType) {
            return isAssignable(getRawType((ParameterizedType) type), oclass);
        } else if (type instanceof TypeVariable) {
            Type[] atype = ((TypeVariable) type).getBounds();
            int i = atype.length;

            for (int j = 0; j < i; ++j) {
                Type type1 = atype[j];

                if (isAssignable(type1, oclass)) {
                    return true;
                }
            }

            return false;
        } else if (!(type instanceof GenericArrayType)) {
            if (type instanceof WildcardType) {
                return false;
            } else {
                throw new IllegalStateException("found an unhandled type: " + type);
            }
        } else {
            return oclass.equals(Object.class) || oclass.isArray() && isAssignable(((GenericArrayType) type).getGenericComponentType(), oclass.getComponentType());
        }
    }

    private static boolean isAssignable(Type type, ParameterizedType parameterizedtype, Map map) {
        if (type == null) {
            return true;
        } else if (parameterizedtype == null) {
            return false;
        } else if (parameterizedtype.equals(type)) {
            return true;
        } else {
            Class oclass = getRawType(parameterizedtype);
            Map map1 = getTypeArguments(type, oclass, (Map) null);

            if (map1 == null) {
                return false;
            } else if (map1.isEmpty()) {
                return true;
            } else {
                Map map2 = getTypeArguments(parameterizedtype, oclass, map);
                Iterator iterator = map2.keySet().iterator();

                Type type1;
                Type type2;

                do {
                    do {
                        do {
                            if (!iterator.hasNext()) {
                                return true;
                            }

                            TypeVariable typevariable = (TypeVariable) iterator.next();

                            type1 = unrollVariableAssignments(typevariable, map2);
                            type2 = unrollVariableAssignments(typevariable, map1);
                        } while (type2 == null);
                    } while (type1.equals(type2));
                } while (type1 instanceof WildcardType && isAssignable(type2, type1, map));

                return false;
            }
        }
    }

    private static Type unrollVariableAssignments(TypeVariable typevariable, Map map) {
        while (true) {
            Type type = (Type) map.get(typevariable);

            if (!(type instanceof TypeVariable) || type.equals(typevariable)) {
                return type;
            }

            typevariable = (TypeVariable) type;
        }
    }

    private static boolean isAssignable(Type type, GenericArrayType genericarraytype, Map map) {
        if (type == null) {
            return true;
        } else if (genericarraytype == null) {
            return false;
        } else if (genericarraytype.equals(type)) {
            return true;
        } else {
            Type type1 = genericarraytype.getGenericComponentType();

            if (!(type instanceof Class)) {
                if (type instanceof GenericArrayType) {
                    return isAssignable(((GenericArrayType) type).getGenericComponentType(), type1, map);
                } else {
                    int i;
                    int j;
                    Type type2;
                    Type[] atype;

                    if (type instanceof WildcardType) {
                        atype = getImplicitUpperBounds((WildcardType) type);
                        i = atype.length;

                        for (j = 0; j < i; ++j) {
                            type2 = atype[j];
                            if (isAssignable(type2, (Type) genericarraytype)) {
                                return true;
                            }
                        }

                        return false;
                    } else if (type instanceof TypeVariable) {
                        atype = getImplicitBounds((TypeVariable) type);
                        i = atype.length;

                        for (j = 0; j < i; ++j) {
                            type2 = atype[j];
                            if (isAssignable(type2, (Type) genericarraytype)) {
                                return true;
                            }
                        }

                        return false;
                    } else if (type instanceof ParameterizedType) {
                        return false;
                    } else {
                        throw new IllegalStateException("found an unhandled type: " + type);
                    }
                }
            } else {
                Class oclass = (Class) type;

                return oclass.isArray() && isAssignable(oclass.getComponentType(), type1, map);
            }
        }
    }

    private static boolean isAssignable(Type type, WildcardType wildcardtype, Map map) {
        if (type == null) {
            return true;
        } else if (wildcardtype == null) {
            return false;
        } else if (wildcardtype.equals(type)) {
            return true;
        } else {
            Type[] atype = getImplicitUpperBounds(wildcardtype);
            Type[] atype1 = getImplicitLowerBounds(wildcardtype);

            if (!(type instanceof WildcardType)) {
                Type[] atype2 = atype;
                int i = atype.length;

                int j;
                Type type1;

                for (j = 0; j < i; ++j) {
                    type1 = atype2[j];
                    if (!isAssignable(type, substituteTypeVariables(type1, map), map)) {
                        return false;
                    }
                }

                atype2 = atype1;
                i = atype1.length;

                for (j = 0; j < i; ++j) {
                    type1 = atype2[j];
                    if (!isAssignable(substituteTypeVariables(type1, map), type, map)) {
                        return false;
                    }
                }

                return true;
            } else {
                WildcardType wildcardtype1 = (WildcardType) type;
                Type[] atype3 = getImplicitUpperBounds(wildcardtype1);
                Type[] atype4 = getImplicitLowerBounds(wildcardtype1);
                Type[] atype5 = atype;
                int k = atype.length;

                int l;
                Type type2;
                Type[] atype6;
                int i1;
                int j1;
                Type type3;

                for (l = 0; l < k; ++l) {
                    type2 = atype5[l];
                    type2 = substituteTypeVariables(type2, map);
                    atype6 = atype3;
                    i1 = atype3.length;

                    for (j1 = 0; j1 < i1; ++j1) {
                        type3 = atype6[j1];
                        if (!isAssignable(type3, type2, map)) {
                            return false;
                        }
                    }
                }

                atype5 = atype1;
                k = atype1.length;

                for (l = 0; l < k; ++l) {
                    type2 = atype5[l];
                    type2 = substituteTypeVariables(type2, map);
                    atype6 = atype4;
                    i1 = atype4.length;

                    for (j1 = 0; j1 < i1; ++j1) {
                        type3 = atype6[j1];
                        if (!isAssignable(type2, type3, map)) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }

    private static boolean isAssignable(Type type, TypeVariable typevariable, Map map) {
        if (type == null) {
            return true;
        } else if (typevariable == null) {
            return false;
        } else if (typevariable.equals(type)) {
            return true;
        } else {
            if (type instanceof TypeVariable) {
                Type[] atype = getImplicitBounds((TypeVariable) type);
                Type[] atype1 = atype;
                int i = atype.length;

                for (int j = 0; j < i; ++j) {
                    Type type1 = atype1[j];

                    if (isAssignable(type1, typevariable, map)) {
                        return true;
                    }
                }
            }

            if (!(type instanceof Class) && !(type instanceof ParameterizedType) && !(type instanceof GenericArrayType) && !(type instanceof WildcardType)) {
                throw new IllegalStateException("found an unhandled type: " + type);
            } else {
                return false;
            }
        }
    }

    private static Type substituteTypeVariables(Type type, Map map) {
        if (type instanceof TypeVariable && map != null) {
            Type type1 = (Type) map.get(type);

            if (type1 == null) {
                throw new IllegalArgumentException("missing assignment type for type variable " + type);
            } else {
                return type1;
            }
        } else {
            return type;
        }
    }

    public static Map getTypeArguments(ParameterizedType parameterizedtype) {
        return getTypeArguments(parameterizedtype, getRawType(parameterizedtype), (Map) null);
    }

    public static Map getTypeArguments(Type type, Class oclass) {
        return getTypeArguments(type, oclass, (Map) null);
    }

    private static Map getTypeArguments(Type type, Class oclass, Map map) {
        if (type instanceof Class) {
            return getTypeArguments((Class) type, oclass, map);
        } else if (type instanceof ParameterizedType) {
            return getTypeArguments((ParameterizedType) type, oclass, map);
        } else if (type instanceof GenericArrayType) {
            return getTypeArguments(((GenericArrayType) type).getGenericComponentType(), oclass.isArray() ? oclass.getComponentType() : oclass, map);
        } else {
            Type[] atype;
            int i;
            int j;
            Type type1;

            if (type instanceof WildcardType) {
                atype = getImplicitUpperBounds((WildcardType) type);
                i = atype.length;

                for (j = 0; j < i; ++j) {
                    type1 = atype[j];
                    if (isAssignable(type1, oclass)) {
                        return getTypeArguments(type1, oclass, map);
                    }
                }

                return null;
            } else if (type instanceof TypeVariable) {
                atype = getImplicitBounds((TypeVariable) type);
                i = atype.length;

                for (j = 0; j < i; ++j) {
                    type1 = atype[j];
                    if (isAssignable(type1, oclass)) {
                        return getTypeArguments(type1, oclass, map);
                    }
                }

                return null;
            } else {
                throw new IllegalStateException("found an unhandled type: " + type);
            }
        }
    }

    private static Map getTypeArguments(ParameterizedType parameterizedtype, Class oclass, Map map) {
        Class oclass1 = getRawType(parameterizedtype);

        if (!isAssignable(oclass1, oclass)) {
            return null;
        } else {
            Type type = parameterizedtype.getOwnerType();
            Object object;

            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedtype1 = (ParameterizedType) type;

                object = getTypeArguments(parameterizedtype1, getRawType(parameterizedtype1), map);
            } else {
                object = map == null ? new HashMap() : new HashMap(map);
            }

            Type[] atype = parameterizedtype.getActualTypeArguments();
            TypeVariable[] atypevariable = oclass1.getTypeParameters();

            for (int i = 0; i < atypevariable.length; ++i) {
                Type type1 = atype[i];

                ((Map) object).put(atypevariable[i], ((Map) object).containsKey(type1) ? (Type) ((Map) object).get(type1) : type1);
            }

            if (oclass.equals(oclass1)) {
                return (Map) object;
            } else {
                return getTypeArguments(getClosestParentType(oclass1, oclass), oclass, (Map) object);
            }
        }
    }

    private static Map getTypeArguments(Class oclass, Class oclass1, Map map) {
        if (!isAssignable(oclass, oclass1)) {
            return null;
        } else {
            if (oclass.isPrimitive()) {
                if (oclass1.isPrimitive()) {
                    return new HashMap();
                }

                oclass = ClassUtils.primitiveToWrapper(oclass);
            }

            HashMap hashmap = map == null ? new HashMap() : new HashMap(map);

            return (Map) (oclass1.equals(oclass) ? hashmap : getTypeArguments(getClosestParentType(oclass, oclass1), oclass1, hashmap));
        }
    }

    public static Map determineTypeArguments(Class oclass, ParameterizedType parameterizedtype) {
        Validate.notNull(oclass, "cls is null", new Object[0]);
        Validate.notNull(parameterizedtype, "superType is null", new Object[0]);
        Class oclass1 = getRawType(parameterizedtype);

        if (!isAssignable(oclass, oclass1)) {
            return null;
        } else if (oclass.equals(oclass1)) {
            return getTypeArguments(parameterizedtype, oclass1, (Map) null);
        } else {
            Type type = getClosestParentType(oclass, oclass1);

            if (type instanceof Class) {
                return determineTypeArguments((Class) type, parameterizedtype);
            } else {
                ParameterizedType parameterizedtype1 = (ParameterizedType) type;
                Class oclass2 = getRawType(parameterizedtype1);
                Map map = determineTypeArguments(oclass2, parameterizedtype);

                mapTypeVariablesToArguments(oclass, parameterizedtype1, map);
                return map;
            }
        }
    }

    private static void mapTypeVariablesToArguments(Class oclass, ParameterizedType parameterizedtype, Map map) {
        Type type = parameterizedtype.getOwnerType();

        if (type instanceof ParameterizedType) {
            mapTypeVariablesToArguments(oclass, (ParameterizedType) type, map);
        }

        Type[] atype = parameterizedtype.getActualTypeArguments();
        TypeVariable[] atypevariable = getRawType(parameterizedtype).getTypeParameters();
        List list = Arrays.asList(oclass.getTypeParameters());

        for (int i = 0; i < atype.length; ++i) {
            TypeVariable typevariable = atypevariable[i];
            Type type1 = atype[i];

            if (list.contains(type1) && map.containsKey(typevariable)) {
                map.put((TypeVariable) type1, map.get(typevariable));
            }
        }

    }

    private static Type getClosestParentType(Class oclass, Class oclass1) {
        if (oclass1.isInterface()) {
            Type[] atype = oclass.getGenericInterfaces();
            Type type = null;
            Type[] atype1 = atype;
            int i = atype.length;

            for (int j = 0; j < i; ++j) {
                Type type1 = atype1[j];
                Class oclass2 = null;

                if (type1 instanceof ParameterizedType) {
                    oclass2 = getRawType((ParameterizedType) type1);
                } else {
                    if (!(type1 instanceof Class)) {
                        throw new IllegalStateException("Unexpected generic interface type found: " + type1);
                    }

                    oclass2 = (Class) type1;
                }

                if (isAssignable(oclass2, oclass1) && isAssignable(type, (Type) oclass2)) {
                    type = type1;
                }
            }

            if (type != null) {
                return type;
            }
        }

        return oclass.getGenericSuperclass();
    }

    public static boolean isInstance(Object object, Type type) {
        return type == null ? false : (object == null ? !(type instanceof Class) || !((Class) type).isPrimitive() : isAssignable(object.getClass(), type, (Map) null));
    }

    public static Type[] normalizeUpperBounds(Type[] atype) {
        Validate.notNull(atype, "null value specified for bounds array", new Object[0]);
        if (atype.length < 2) {
            return atype;
        } else {
            HashSet hashset = new HashSet(atype.length);
            Type[] atype1 = atype;
            int i = atype.length;
            int j = 0;

            while (j < i) {
                Type type = atype1[j];
                boolean flag = false;
                Type[] atype2 = atype;
                int k = atype.length;
                int l = 0;

                while (true) {
                    if (l < k) {
                        Type type1 = atype2[l];

                        if (type == type1 || !isAssignable(type1, type, (Map) null)) {
                            ++l;
                            continue;
                        }

                        flag = true;
                    }

                    if (!flag) {
                        hashset.add(type);
                    }

                    ++j;
                    break;
                }
            }

            return (Type[]) hashset.toArray(new Type[hashset.size()]);
        }
    }

    public static Type[] getImplicitBounds(TypeVariable typevariable) {
        Validate.notNull(typevariable, "typeVariable is null", new Object[0]);
        Type[] atype = typevariable.getBounds();

        return atype.length == 0 ? new Type[] { Object.class} : normalizeUpperBounds(atype);
    }

    public static Type[] getImplicitUpperBounds(WildcardType wildcardtype) {
        Validate.notNull(wildcardtype, "wildcardType is null", new Object[0]);
        Type[] atype = wildcardtype.getUpperBounds();

        return atype.length == 0 ? new Type[] { Object.class} : normalizeUpperBounds(atype);
    }

    public static Type[] getImplicitLowerBounds(WildcardType wildcardtype) {
        Validate.notNull(wildcardtype, "wildcardType is null", new Object[0]);
        Type[] atype = wildcardtype.getLowerBounds();

        return atype.length == 0 ? new Type[] { null} : atype;
    }

    public static boolean typesSatisfyVariables(Map map) {
        Validate.notNull(map, "typeVarAssigns is null", new Object[0]);
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            TypeVariable typevariable = (TypeVariable) entry.getKey();
            Type type = (Type) entry.getValue();
            Type[] atype = getImplicitBounds(typevariable);
            int i = atype.length;

            for (int j = 0; j < i; ++j) {
                Type type1 = atype[j];

                if (!isAssignable(type, substituteTypeVariables(type1, map), map)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static Class getRawType(ParameterizedType parameterizedtype) {
        Type type = parameterizedtype.getRawType();

        if (!(type instanceof Class)) {
            throw new IllegalStateException("Wait... What!? Type of rawType: " + type);
        } else {
            return (Class) type;
        }
    }

    public static Class getRawType(Type type, Type type1) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getRawType((ParameterizedType) type);
        } else if (type instanceof TypeVariable) {
            if (type1 == null) {
                return null;
            } else {
                GenericDeclaration genericdeclaration = ((TypeVariable) type).getGenericDeclaration();

                if (!(genericdeclaration instanceof Class)) {
                    return null;
                } else {
                    Map map = getTypeArguments(type1, (Class) genericdeclaration);

                    if (map == null) {
                        return null;
                    } else {
                        Type type2 = (Type) map.get(type);

                        return type2 == null ? null : getRawType(type2, type1);
                    }
                }
            }
        } else if (type instanceof GenericArrayType) {
            Class oclass = getRawType(((GenericArrayType) type).getGenericComponentType(), type1);

            return Array.newInstance(oclass, 0).getClass();
        } else if (type instanceof WildcardType) {
            return null;
        } else {
            throw new IllegalArgumentException("unknown type: " + type);
        }
    }

    public static boolean isArrayType(Type type) {
        return type instanceof GenericArrayType || type instanceof Class && ((Class) type).isArray();
    }

    public static Type getArrayComponentType(Type type) {
        if (type instanceof Class) {
            Class oclass = (Class) type;

            return oclass.isArray() ? oclass.getComponentType() : null;
        } else {
            return type instanceof GenericArrayType ? ((GenericArrayType) type).getGenericComponentType() : null;
        }
    }

    public static Type unrollVariables(Map map, Type type) {
        if (map == null) {
            map = Collections.emptyMap();
        }

        if (containsTypeVariables(type)) {
            if (type instanceof TypeVariable) {
                return unrollVariables(map, (Type) map.get(type));
            }

            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedtype = (ParameterizedType) type;
                Object object;

                if (parameterizedtype.getOwnerType() == null) {
                    object = map;
                } else {
                    object = new HashMap(map);
                    ((Map) object).putAll(getTypeArguments(parameterizedtype));
                }

                Type[] atype = parameterizedtype.getActualTypeArguments();

                for (int i = 0; i < atype.length; ++i) {
                    Type type1 = unrollVariables((Map) object, atype[i]);

                    if (type1 != null) {
                        atype[i] = type1;
                    }
                }

                return parameterizeWithOwner(parameterizedtype.getOwnerType(), (Class) parameterizedtype.getRawType(), atype);
            }

            if (type instanceof WildcardType) {
                WildcardType wildcardtype = (WildcardType) type;

                return wildcardType().withUpperBounds(unrollBounds(map, wildcardtype.getUpperBounds())).withLowerBounds(unrollBounds(map, wildcardtype.getLowerBounds())).build();
            }
        }

        return type;
    }

    private static Type[] unrollBounds(Map map, Type[] atype) {
        Type[] atype1 = atype;

        for (int i = 0; i < atype1.length; ++i) {
            Type type = unrollVariables(map, atype1[i]);

            if (type == null) {
                atype1 = (Type[]) ArrayUtils.remove((Object[]) atype1, i--);
            } else {
                atype1[i] = type;
            }
        }

        return atype1;
    }

    public static boolean containsTypeVariables(Type type) {
        if (type instanceof TypeVariable) {
            return true;
        } else if (type instanceof Class) {
            return ((Class) type).getTypeParameters().length > 0;
        } else if (type instanceof ParameterizedType) {
            Type[] atype = ((ParameterizedType) type).getActualTypeArguments();
            int i = atype.length;

            for (int j = 0; j < i; ++j) {
                Type type1 = atype[j];

                if (containsTypeVariables(type1)) {
                    return true;
                }
            }

            return false;
        } else if (!(type instanceof WildcardType)) {
            return false;
        } else {
            WildcardType wildcardtype = (WildcardType) type;

            return containsTypeVariables(getImplicitLowerBounds(wildcardtype)[0]) || containsTypeVariables(getImplicitUpperBounds(wildcardtype)[0]);
        }
    }

    public static final ParameterizedType parameterize(Class oclass, Type... atype) {
        return parameterizeWithOwner((Type) null, oclass, atype);
    }

    public static final ParameterizedType parameterize(Class oclass, Map map) {
        Validate.notNull(oclass, "raw class is null", new Object[0]);
        Validate.notNull(map, "typeArgMappings is null", new Object[0]);
        return parameterizeWithOwner((Type) null, oclass, extractTypeArgumentsFrom(map, oclass.getTypeParameters()));
    }

    public static final ParameterizedType parameterizeWithOwner(Type type, Class oclass, Type... atype) {
        Validate.notNull(oclass, "raw class is null", new Object[0]);
        Object object;

        if (oclass.getEnclosingClass() == null) {
            Validate.isTrue(type == null, "no owner allowed for top-level %s", new Object[] { oclass});
            object = null;
        } else if (type == null) {
            object = oclass.getEnclosingClass();
        } else {
            Validate.isTrue(isAssignable(type, oclass.getEnclosingClass()), "%s is invalid owner type for parameterized %s", new Object[] { type, oclass});
            object = type;
        }

        Validate.noNullElements((Object[]) atype, "null type argument at index %s", new Object[0]);
        Validate.isTrue(oclass.getTypeParameters().length == atype.length, "invalid number of type parameters specified: expected %s, got %s", new Object[] { Integer.valueOf(oclass.getTypeParameters().length), Integer.valueOf(atype.length)});
        return new TypeUtils.ParameterizedTypeImpl(oclass, (Type) object, atype, null);
    }

    public static final ParameterizedType parameterizeWithOwner(Type type, Class oclass, Map map) {
        Validate.notNull(oclass, "raw class is null", new Object[0]);
        Validate.notNull(map, "typeArgMappings is null", new Object[0]);
        return parameterizeWithOwner(type, oclass, extractTypeArgumentsFrom(map, oclass.getTypeParameters()));
    }

    private static Type[] extractTypeArgumentsFrom(Map map, TypeVariable[] atypevariable) {
        Type[] atype = new Type[atypevariable.length];
        int i = 0;
        TypeVariable[] atypevariable1 = atypevariable;
        int j = atypevariable.length;

        for (int k = 0; k < j; ++k) {
            TypeVariable typevariable = atypevariable1[k];

            Validate.isTrue(map.containsKey(typevariable), "missing argument mapping for %s", new Object[] { toString(typevariable)});
            atype[i++] = (Type) map.get(typevariable);
        }

        return atype;
    }

    public static TypeUtils.WildcardTypeBuilder wildcardType() {
        return new TypeUtils.WildcardTypeBuilder(null);
    }

    public static GenericArrayType genericArrayType(Type type) {
        return new TypeUtils.GenericArrayTypeImpl((Type) Validate.notNull(type, "componentType is null", new Object[0]), null);
    }

    public static boolean equals(Type type, Type type1) {
        return ObjectUtils.equals(type, type1) ? true : (type instanceof ParameterizedType ? equals((ParameterizedType) type, type1) : (type instanceof GenericArrayType ? equals((GenericArrayType) type, type1) : (type instanceof WildcardType ? equals((WildcardType) type, type1) : false)));
    }

    private static boolean equals(ParameterizedType parameterizedtype, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedtype1 = (ParameterizedType) type;

            if (equals(parameterizedtype.getRawType(), parameterizedtype1.getRawType()) && equals(parameterizedtype.getOwnerType(), parameterizedtype1.getOwnerType())) {
                return equals(parameterizedtype.getActualTypeArguments(), parameterizedtype1.getActualTypeArguments());
            }
        }

        return false;
    }

    private static boolean equals(GenericArrayType genericarraytype, Type type) {
        return type instanceof GenericArrayType && equals(genericarraytype.getGenericComponentType(), ((GenericArrayType) type).getGenericComponentType());
    }

    private static boolean equals(WildcardType wildcardtype, Type type) {
        if (!(type instanceof WildcardType)) {
            return true;
        } else {
            WildcardType wildcardtype1 = (WildcardType) type;

            return equals(wildcardtype.getLowerBounds(), wildcardtype1.getLowerBounds()) && equals(getImplicitUpperBounds(wildcardtype), getImplicitUpperBounds(wildcardtype1));
        }
    }

    private static boolean equals(Type[] atype, Type[] atype1) {
        if (atype.length == atype1.length) {
            for (int i = 0; i < atype.length; ++i) {
                if (!equals(atype[i], atype1[i])) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static String toString(Type type) {
        Validate.notNull(type);
        if (type instanceof Class) {
            return classToString((Class) type);
        } else if (type instanceof ParameterizedType) {
            return parameterizedTypeToString((ParameterizedType) type);
        } else if (type instanceof WildcardType) {
            return wildcardTypeToString((WildcardType) type);
        } else if (type instanceof TypeVariable) {
            return typeVariableToString((TypeVariable) type);
        } else if (type instanceof GenericArrayType) {
            return genericArrayTypeToString((GenericArrayType) type);
        } else {
            throw new IllegalArgumentException(ObjectUtils.identityToString(type));
        }
    }

    public static String toLongString(TypeVariable typevariable) {
        Validate.notNull(typevariable, "var is null", new Object[0]);
        StringBuilder stringbuilder = new StringBuilder();
        GenericDeclaration genericdeclaration = typevariable.getGenericDeclaration();

        if (genericdeclaration instanceof Class) {
            Class oclass;

            for (oclass = (Class) genericdeclaration; oclass.getEnclosingClass() != null; oclass = oclass.getEnclosingClass()) {
                stringbuilder.insert(0, oclass.getSimpleName()).insert(0, '.');
            }

            stringbuilder.insert(0, oclass.getName());
        } else if (genericdeclaration instanceof Type) {
            stringbuilder.append(toString((Type) genericdeclaration));
        } else {
            stringbuilder.append(genericdeclaration);
        }

        return stringbuilder.append(':').append(typeVariableToString(typevariable)).toString();
    }

    public static Typed wrap(final Type type) {
        return new Typed() {
            public Type getType() {
                return type;
            }
        };
    }

    public static Typed wrap(Class oclass) {
        return wrap((Type) oclass);
    }

    private static String classToString(Class oclass) {
        StringBuilder stringbuilder = new StringBuilder();

        if (oclass.getEnclosingClass() != null) {
            stringbuilder.append(classToString(oclass.getEnclosingClass())).append('.').append(oclass.getSimpleName());
        } else {
            stringbuilder.append(oclass.getName());
        }

        if (oclass.getTypeParameters().length > 0) {
            stringbuilder.append('<');
            appendAllTo(stringbuilder, ", ", oclass.getTypeParameters());
            stringbuilder.append('>');
        }

        return stringbuilder.toString();
    }

    private static String typeVariableToString(TypeVariable typevariable) {
        StringBuilder stringbuilder = new StringBuilder(typevariable.getName());
        Type[] atype = typevariable.getBounds();

        if (atype.length > 0 && (atype.length != 1 || !Object.class.equals(atype[0]))) {
            stringbuilder.append(" extends ");
            appendAllTo(stringbuilder, " & ", typevariable.getBounds());
        }

        return stringbuilder.toString();
    }

    private static String parameterizedTypeToString(ParameterizedType parameterizedtype) {
        StringBuilder stringbuilder = new StringBuilder();
        Type type = parameterizedtype.getOwnerType();
        Class oclass = (Class) parameterizedtype.getRawType();
        Type[] atype = parameterizedtype.getActualTypeArguments();

        if (type == null) {
            stringbuilder.append(oclass.getName());
        } else {
            if (type instanceof Class) {
                stringbuilder.append(((Class) type).getName());
            } else {
                stringbuilder.append(type.toString());
            }

            stringbuilder.append('.').append(oclass.getSimpleName());
        }

        appendAllTo(stringbuilder.append('<'), ", ", atype).append('>');
        return stringbuilder.toString();
    }

    private static String wildcardTypeToString(WildcardType wildcardtype) {
        StringBuilder stringbuilder = (new StringBuilder()).append('?');
        Type[] atype = wildcardtype.getLowerBounds();
        Type[] atype1 = wildcardtype.getUpperBounds();

        if (atype.length > 0) {
            appendAllTo(stringbuilder.append(" super "), " & ", atype);
        } else if (atype1.length != 1 || !Object.class.equals(atype1[0])) {
            appendAllTo(stringbuilder.append(" extends "), " & ", atype1);
        }

        return stringbuilder.toString();
    }

    private static String genericArrayTypeToString(GenericArrayType genericarraytype) {
        return String.format("%s[]", new Object[] { toString(genericarraytype.getGenericComponentType())});
    }

    private static StringBuilder appendAllTo(StringBuilder stringbuilder, String s, Type... atype) {
        Validate.notEmpty(Validate.noNullElements((Object[]) atype));
        if (atype.length > 0) {
            stringbuilder.append(toString(atype[0]));

            for (int i = 1; i < atype.length; ++i) {
                stringbuilder.append(s).append(toString(atype[i]));
            }
        }

        return stringbuilder;
    }

    private static final class WildcardTypeImpl implements WildcardType {

        private static final Type[] EMPTY_BOUNDS = new Type[0];
        private final Type[] upperBounds;
        private final Type[] lowerBounds;

        private WildcardTypeImpl(Type[] atype, Type[] atype1) {
            this.upperBounds = (Type[]) ObjectUtils.defaultIfNull(atype, TypeUtils.WildcardTypeImpl.EMPTY_BOUNDS);
            this.lowerBounds = (Type[]) ObjectUtils.defaultIfNull(atype1, TypeUtils.WildcardTypeImpl.EMPTY_BOUNDS);
        }

        public Type[] getUpperBounds() {
            return (Type[]) this.upperBounds.clone();
        }

        public Type[] getLowerBounds() {
            return (Type[]) this.lowerBounds.clone();
        }

        public String toString() {
            return TypeUtils.toString(this);
        }

        public boolean equals(Object object) {
            return object == this || object instanceof WildcardType && TypeUtils.equals((WildcardType) this, (Type) ((WildcardType) object));
        }

        public int hashCode() {
            short short0 = 18688;
            int i = short0 | Arrays.hashCode(this.upperBounds);

            i <<= 8;
            i |= Arrays.hashCode(this.lowerBounds);
            return i;
        }

        WildcardTypeImpl(Type[] atype, Type[] atype1, Object object) {
            this(atype, atype1);
        }
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType {

        private final Class raw;
        private final Type useOwner;
        private final Type[] typeArguments;

        private ParameterizedTypeImpl(Class oclass, Type type, Type[] atype) {
            this.raw = oclass;
            this.useOwner = type;
            this.typeArguments = atype;
        }

        public Type getRawType() {
            return this.raw;
        }

        public Type getOwnerType() {
            return this.useOwner;
        }

        public Type[] getActualTypeArguments() {
            return (Type[]) this.typeArguments.clone();
        }

        public String toString() {
            return TypeUtils.toString(this);
        }

        public boolean equals(Object object) {
            return object == this || object instanceof ParameterizedType && TypeUtils.equals((ParameterizedType) this, (Type) ((ParameterizedType) object));
        }

        public int hashCode() {
            short short0 = 1136;
            int i = short0 | this.raw.hashCode();

            i <<= 4;
            i |= ObjectUtils.hashCode(this.useOwner);
            i <<= 8;
            i |= Arrays.hashCode(this.typeArguments);
            return i;
        }

        ParameterizedTypeImpl(Class oclass, Type type, Type[] atype, Object object) {
            this(oclass, type, atype);
        }
    }

    private static final class GenericArrayTypeImpl implements GenericArrayType {

        private final Type componentType;

        private GenericArrayTypeImpl(Type type) {
            this.componentType = type;
        }

        public Type getGenericComponentType() {
            return this.componentType;
        }

        public String toString() {
            return TypeUtils.toString(this);
        }

        public boolean equals(Object object) {
            return object == this || object instanceof GenericArrayType && TypeUtils.equals((GenericArrayType) this, (Type) ((GenericArrayType) object));
        }

        public int hashCode() {
            short short0 = 1072;
            int i = short0 | this.componentType.hashCode();

            return i;
        }

        GenericArrayTypeImpl(Type type, Object object) {
            this(type);
        }
    }

    public static class WildcardTypeBuilder implements Builder {

        private Type[] upperBounds;
        private Type[] lowerBounds;

        private WildcardTypeBuilder() {}

        public TypeUtils.WildcardTypeBuilder withUpperBounds(Type... atype) {
            this.upperBounds = atype;
            return this;
        }

        public TypeUtils.WildcardTypeBuilder withLowerBounds(Type... atype) {
            this.lowerBounds = atype;
            return this;
        }

        public WildcardType build() {
            return new TypeUtils.WildcardTypeImpl(this.upperBounds, this.lowerBounds, null);
        }

        WildcardTypeBuilder(Object object) {
            this();
        }
    }
}

/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.csdev.ebus.command.datatypes;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.csdev.ebus.command.datatypes.ext.EBusTypeBytes;
import de.csdev.ebus.command.datatypes.ext.EBusTypeDateTime;
import de.csdev.ebus.command.datatypes.ext.EBusTypeKWCrc;
import de.csdev.ebus.command.datatypes.ext.EBusTypeMultiWord;
import de.csdev.ebus.command.datatypes.ext.EBusTypeString;
import de.csdev.ebus.command.datatypes.std.EBusTypeBCD;
import de.csdev.ebus.command.datatypes.std.EBusTypeBit;
import de.csdev.ebus.command.datatypes.std.EBusTypeByte;
import de.csdev.ebus.command.datatypes.std.EBusTypeChar;
import de.csdev.ebus.command.datatypes.std.EBusTypeData1b;
import de.csdev.ebus.command.datatypes.std.EBusTypeData1c;
import de.csdev.ebus.command.datatypes.std.EBusTypeData2b;
import de.csdev.ebus.command.datatypes.std.EBusTypeData2c;
import de.csdev.ebus.command.datatypes.std.EBusTypeInteger;
import de.csdev.ebus.command.datatypes.std.EBusTypeWord;

/**
 * @author Christian Sowada - Initial contribution
 *
 */
public class EBusTypeRegistry {

    private static final Logger logger = LoggerFactory.getLogger(EBusTypeRegistry.class);

    private Map<String, IEBusType<?>> types = null;

    public EBusTypeRegistry() {
        init();
    }

    protected void init() {
        types = new HashMap<String, IEBusType<?>>();

        add(EBusTypeBit.class);
        add(EBusTypeByte.class);
        add(EBusTypeChar.class);
        add(EBusTypeInteger.class);
        add(EBusTypeWord.class);
        add(EBusTypeBCD.class);
        add(EBusTypeData1b.class);
        add(EBusTypeData1c.class);
        add(EBusTypeData2b.class);
        add(EBusTypeData2c.class);

        add(EBusTypeBytes.class);
        add(EBusTypeString.class);

        add(EBusTypeMultiWord.class);
        add(EBusTypeDateTime.class);

        // ext
        add(EBusTypeKWCrc.class);
    }

    // public <T extends IEBusType> T xxx(T xxxy) {
    // return xxxy;
    // }

    public <T> IEBusType<T> getType(String type, Map<String, Object> properties) {
        IEBusType<T> ebusType = getType(type);

        if (ebusType != null) {
            return ebusType.getInstance(properties);
        }

        return null;
    }

    public <T> IEBusType<T> getType(String type) {
        @SuppressWarnings("unchecked")
        IEBusType<T> eBusType = (IEBusType<T>) types.get(type);

        if (eBusType == null) {
            logger.warn("No eBUS data type with name {} !", type);
            return null;
        }

        return eBusType;
    }

    public byte[] encode(String type, Object data, Object... args) throws EBusTypeException {
        IEBusType<?> eBusType = types.get(type);

        if (eBusType == null) {
            logger.warn("No eBUS data type with name {} !", type);
            return null;
        }

        return eBusType.encode(data);
    }

    public <T> T decode(String type, byte[] data) throws EBusTypeException {
        @SuppressWarnings("unchecked")
        IEBusType<T> eBusType = (IEBusType<T>) types.get(type);

        if (eBusType == null) {
            logger.warn("No eBUS data type with name {} !", type);
            return null;
        }

        return eBusType.decode(data);
    }

    public void add(Class<?> clazz) {
        try {
            IEBusType<?> newInstance = (IEBusType<?>) clazz.newInstance();
            newInstance.setTypesParent(this);

            for (String typeName : newInstance.getSupportedTypes()) {
                logger.trace("Add eBUS type {}", typeName);
                types.put(typeName, newInstance);
            }

        } catch (InstantiationException e) {
            logger.error("error!", e);

        } catch (IllegalAccessException e) {
            logger.error("error!", e);
        }
    }

    @Override
    public String toString() {
        return "EBusTypeRegistry [types=" + types + "]";
    }

}
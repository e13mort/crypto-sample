/**
 * Copyright 2004-2013 Crypto-Pro. All rights reserved.
 * Программный код, содержащийся в этом файле, предназначен
 * для целей обучения. Может быть скопирован или модифицирован
 * при условии сохранения абзацев с указанием авторства и прав.
 *
 * Данный код не может быть непосредственно использован
 * для защиты информации. Компания Крипто-Про не несет никакой
 * ответственности за функционирование этого кода.
 */
package ru.CryptoPro.ACSPClientApp.util;

import android.content.Context;

import java.io.IOException;
import java.security.Provider;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ru.CryptoPro.JCSP.JCSP;

/**
 * Служебный класс KeyStoreType предназначен
 * для загрузки/сохранения номера типа хранилища
 * в файл. Используется только в demo-приложении.
 *
 * 26/08/2013
 *
 */
public final class KeyStoreType extends ArrayResourceSelector {

    /**
     * Название файла для сохранения ресурсов.
     */
    private static final String KEY_TYPE_RESOURCE_NAME = "keyStores";

    /**
     * Объект для управления загрузки/сохранения
     * активного типа контейнера.
     *
     */
    private static KeyStoreType keyStoreType_ = null;

    /**
     * Проверка инициализации.
     */
    private static boolean initiated = false;

    /**
     * Инициализация объекта для работы с типами
     * контейнеров.
     *
     * @param context Контекст приложения.
     */
    public synchronized static void init(Context context) {

        if (!initiated) {

            try {
                keyStoreType_ = new KeyStoreType(context);
                initiated = true;
            } catch (IOException e) {
                ;
            }

        } // if
    }

    /**
     * Конструктор.
     *
     * @param context Контекст приложения.
     * @throws IOException
     */
    private KeyStoreType(Context context) throws IOException {
        super(context, KEY_TYPE_RESOURCE_NAME, getKeyStoreTypeList());
    }

    /**
     * Получение списка поддерживаемых типов контейнеров.
     *
     * @return список типов.
     */
    public static List<String> getKeyStoreTypeList() {

        List<String> keyStoreTypeList = new LinkedList<String>();
        Set<Provider.Service> services = KeyStoreUtil.DEFAULT_PROVIDER.getServices();

        // Список типов контейнеров.
        for (Provider.Service service : services) {
            if (service.getType().equalsIgnoreCase("KeyStore")) {
                keyStoreTypeList.add(service.getAlgorithm());
            } // if
        } // for

        keyStoreTypeList.remove(JCSP.HD_STORE_NAME); // Удалим его, чтобы...
        keyStoreTypeList.add(0, JCSP.HD_STORE_NAME); // поставить на 1 место.

        keyStoreTypeList.remove(JCSP.CERT_STORE_NAME); // А это - не тип контейнера.
        keyStoreTypeList.remove(JCSP.PFX_STORE_NAME);  // Пока не поддерживается передача.

        return keyStoreTypeList;

    }

    /**
     * Получение активного типа хранилища.
     *
     * @return Тип хранилища.
     */
    public static String currentType() {

        if (!initiated) {
            return JCSP.HD_STORE_NAME;
        } // if

        return keyStoreType_.currentValue();
    }

    /**
     * Сохранение типа хранилища в файл.
     *
     * @param type Тип хранилища.
     * @return True в случае успешного сохранения.
     */
    public static boolean saveCurrentType(String type) {

        if (initiated) {
            return keyStoreType_.saveValue(type);
        } // if

        return false;
    }

}

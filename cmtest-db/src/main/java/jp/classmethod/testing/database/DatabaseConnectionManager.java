/*
 * Copyright 2013 Classmethod, Inc.
 * Created on 2013/08/14
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.classmethod.testing.database;

import org.dbunit.database.IDatabaseConnection;

/**
 * {@link DbUnitTester}でDbUnit用のコネクション(IDatabaseConnection)を作成するインターフェイス。
 * 
 * @since 1.0
 * @author shuji
 */
public interface DatabaseConnectionManager {

    /**
     * DbUnitのコネクションを作成する。
     * 
     * @return {@link IDatabaseConnection} オブジェクト
     * @throws Exception
     */
    IDatabaseConnection getConnection() throws Exception;

}

/*
 * Copyright (C) 2016 Qiujuer <qiujuer@live.cn>
 * WebSite http://www.qiujuer.net
 * Created 1/1/2016
 * Changed 1/1/2016
 * Version 1.0.0
 * Author Qiujuer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.dareway.dwlibrary.netutils.okhttp;

import java.io.File;

/**
 * Param to IO , File or stream
 */
public class IOParam {
    public String key;
    public File file;

    public IOParam(String key, File file) {
        this.key = key;
        this.file = file;
    }
}

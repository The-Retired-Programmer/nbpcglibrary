/*
 * Copyright 2017 richard.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.theretiredprogrammer.nbpcglibrary.htmlrest;

/**
 *
 * @author richard
 */
public class ProviderEntity {

    private String shortname;
    private String description;
    private boolean allowpandl;
    private boolean allowbs;
    private Integer id;
    private String createdby;
    private String createdon;
    private String updatedby;
    private String updatedon;

    public ProviderEntity() {
    }

    public ProviderEntity(Integer id) {
        this.id = id;
    }

    public ProviderEntity(Integer id, String shortname, boolean allowpandl, boolean allowbs, String createdby, String createdon, String updatedby, String updatedon) {
        this.id = id;
        this.shortname = shortname;
        this.allowpandl = allowpandl;
        this.allowbs = allowbs;
        this.createdby = createdby;
        this.createdon = createdon;
        this.updatedby = updatedby;
        this.updatedon = updatedon;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getAllowpandl() {
        return allowpandl;
    }

    public void setAllowpandl(boolean allowpandl) {
        this.allowpandl = allowpandl;
    }

    public boolean getAllowbs() {
        return allowbs;
    }

    public void setAllowbs(boolean allowbs) {
        this.allowbs = allowbs;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

}

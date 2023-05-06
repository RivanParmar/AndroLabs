/*
 * Copyright 2023 Rivan Parmar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rivan.androlabs.wizard.template.api

/**
 * Representation of all types of projects we can build. This class is also used in the wizard to
 * show elements in the tabs.
 */
enum class TemplateCategory(val displayName: String) {
    Mobile("Phone and Tablet"),
    Wear("Wear OS"),
    Tv("Television"),
    Automotive("Automotive"),
    Lab("Lab");

    override fun toString(): String = displayName
}